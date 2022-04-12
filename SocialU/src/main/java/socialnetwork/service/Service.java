package socialnetwork.service;

import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import socialnetwork.domain.CereriDTO;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.CerereValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.CereriPrietenieDataBase;
import socialnetwork.repository.database.EvenimenteDataBase;
import socialnetwork.repository.database.GroupDataBase;
import socialnetwork.repository.database.MessageDataBase;
import socialnetwork.utils.Constants;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class Service {
    private Repository<String, Utilizator> repoUser;
    private Repository<Tuple<String>, Prietenie> repoPrietenie;
    private CereriPrietenieDataBase repoCereri;
    private MessageDataBase repoMessage;
    private GroupDataBase repoGrup;
    private EvenimenteDataBase repoEvent;
    private final CerereValidator valCerere =  new CerereValidator();

    /**
     *
     * @param repoUser
     * @param repoPrietenie
     */
    public Service(Repository<String, Utilizator> repoUser, Repository<Tuple<String>, Prietenie> repoPrietenie, CereriPrietenieDataBase repoCereri,MessageDataBase repoMessage, GroupDataBase repoGrup, EvenimenteDataBase repoEvent) {
        this.repoUser = repoUser;
        this.repoPrietenie = repoPrietenie;
        this.repoCereri = repoCereri;
        this.repoMessage = repoMessage;
        this.repoGrup = repoGrup;
        this.repoEvent = repoEvent;
        repoPrietenie.findAll().forEach(prietenie -> {
            repoUser.findOne(prietenie.getId().getLeft()).getFriends().add(prietenie.getId().getRight());
            repoUser.findOne(prietenie.getId().getRight()).getFriends().add(prietenie.getId().getLeft());
        });
    }



    //========================================================================== USER REPO



    /**
     *
     * @param mail
     * @param nume
     * @param prenume
     */
    public void addUtilizator(String mail, String nume, String prenume, String parola) {
        Utilizator user = new Utilizator(mail, nume, prenume, parola);
        repoUser.save(user);

    }

    /**
     *
     * @param mail
     */
    public void delUtilizator(String mail) {
        Utilizator user =repoUser.delete(mail);
        if (user == null){
            return;
        }
        user.getFriends().forEach(friend->{
            repoPrietenie.delete(new Tuple<> (user.getId(),friend));
            repoUser.findOne(friend).getFriends().remove(user.getId());
        });
    }

    /**
     *
     * @return all entities
     */
    public Iterable<Utilizator> getAllUtilizator(){
        return repoUser.findAll();
    }

    public Utilizator searchUtilizator(String email){
        return repoUser.findOne(email);
    }



    //========================================================================== FRIENDSHIP REPOS



    /**
     * Adauga o prietenie
     * @param mail1
     * @param mail2
     */
    public void addPrietene(String mail1, String mail2){
        Utilizator user1 = repoUser.findOne(mail1);
        Utilizator user2 = repoUser.findOne(mail2);
        if(user1 == null || user2 == null){
            throw new ServiceException("Nu exista user");
        }
        Prietenie prietenie = repoPrietenie.save(new Prietenie(new Tuple<> (mail1, mail2), LocalDate.now()));
        if(prietenie == null){
            user1.getFriends().add(user2.getId());
            user2.getFriends().add(user1.getId());
        }
        else{
            throw new ServiceException("Prietenie exista deja");
        }

    }

    /**
     * Sterge o prietenie
     * @param mail1
     * @param mail2
     */
    public void delPrietenie(String mail1, String mail2){
        Utilizator user1 = repoUser.findOne(mail1);
        Utilizator user2 = repoUser.findOne(mail2);

        repoPrietenie.delete(new Tuple<> (mail1, mail2));
        this.updateCereri(mail1, mail2, "rejected");
        repoMessage.deleteChat(mail1, mail2);
    }

    /**
     * Returneaza toate prieteniile
     * @return
     */
    public  Iterable<Prietenie> getAllPrietenie() {
        return repoPrietenie.findAll();
    }

    /**
     * Returneaza toti prietenii unui user, precum si data in care s-au imprietenit
     * @param email
     * @return
     */
    public Iterable<PrietenieDTO> getAllPrieteni(String email){
        HashSet<PrietenieDTO> rez = new HashSet<>();
        HashSet<Prietenie> prietenii = (HashSet<Prietenie>) repoPrietenie.findAll();
        prietenii.stream().filter(x->
                x.getId().getLeft().equals(email) || x.getId().getRight().equals(email)).
                forEach(y->{
                    HashSet<Utilizator> utilizatori = (HashSet<Utilizator>) repoUser.findAll();
                    utilizatori.stream().filter(a->(a.getId().equals(y.getId().getLeft()) || a.getId().equals(y.getId().getRight())) && !a.getId().equals(email)).
                            forEach(p->{
                                PrietenieDTO usr = new PrietenieDTO(p.getFirstName(),p.getLastName(),y.getDate());
                                rez.add(usr);
                            });
                });
        return rez;
    }

    /**
     * Filtrare pt lab
     * @param email
     * @return
     */
    public Iterable<PrietenDTO> getAllPrieteniUser(String email){
        List<PrietenDTO> rez = new ArrayList<>();
        HashSet<Prietenie> prietenii = (HashSet<Prietenie>) repoPrietenie.findAll();
        prietenii.stream().filter(x->
                        x.getId().getLeft().equals(email) || x.getId().getRight().equals(email)).
                forEach(y->{
                    HashSet<Utilizator> utilizatori = (HashSet<Utilizator>) repoUser.findAll();
                    utilizatori.stream().filter(a->(a.getId().equals(y.getId().getLeft()) || a.getId().equals(y.getId().getRight())) && !a.getId().equals(email)).
                            forEach(p->{
                                PrietenDTO usr = new PrietenDTO(p.getId(), p.getFirstName(), p.getLastName());
                                rez.add(usr);
                            });
                });

        for (PrietenDTO pr:rez)
            pr.setData(repoMessage.getLastMessageDate(email, pr.getEmail()));

        Collections.sort(rez);
        Collections.reverse(rez);
        return rez;
    }

    /**
     * Filtrare pt lab
     * @param email
     * @param data
     * @return
     */
    public Iterable<PrietenieDTO> getAllPrieteniLuna(String email, int data){
        HashSet<PrietenieDTO> prietenii = (HashSet<PrietenieDTO>) this.getAllPrieteni(email);
        HashSet<PrietenieDTO> rez = new HashSet<>();
        prietenii.stream().filter(x->x.getData().getMonthValue() == data).
                forEach(x->{
                    PrietenieDTO pr = new PrietenieDTO(x.getNume(),x.getPrenume(),x.getData());
                    rez.add(pr);
                });
        return rez;
    }

    /**
     * Trimite o cerere de prietenie
     * @param fromUser
     * @param toUser
     */
    public void addCerere(String fromUser, String toUser){
        CererePrietenie cr = new CererePrietenie(fromUser,toUser,"pending");
        if(repoCereri.findOne(fromUser,toUser) != null){
            if(repoCereri.findOne(fromUser,toUser).getStatus().equals("rejected")){
                repoCereri.update(cr);
                return;
            }
            else {
                throw new ServiceException("Exista deja cererea!!!");
            }
        }
        else {
            repoCereri.save(cr);
        }
    }

    /**
     * Returneaza toate cererile de prietenie
     * @param email
     * @return
     */
    public Iterable<CereriDTO> getAllCereri(String email){
        HashSet<CereriDTO> rez = new HashSet<>();
        HashSet<CererePrietenie> cereri = (HashSet<CererePrietenie>) repoCereri.findAll();
        cereri.stream().filter(x-> x.getToUser().equals(email) && x.getStatus().equals("pending")).
                forEach(y->{
                    CereriDTO cr = new CereriDTO(y.getFromUser(), repoUser.findOne(y.getFromUser()).getFirstName(),
                            repoUser.findOne(y.getFromUser()).getLastName(), y.getStatus(), y.getData());

                    rez.add(cr);
                });
        return rez;
    }

    /**
     * Modifica starea unei cereri de prietenie
     * @param fromUser
     * @param toUser
     * @param status
     */
    public void updateCereri(String fromUser,String toUser, String status){
        CererePrietenie cr = new CererePrietenie(fromUser,toUser,status);
        valCerere.validate(cr);
        repoCereri.update(cr);
        if(repoCereri.findOne(fromUser,toUser).getStatus().equals("approved")){

            Prietenie pr = new Prietenie(new Tuple<>(fromUser, toUser),LocalDate.now());
            repoPrietenie.save(pr);
        }
    }

    /**
     * Returneaza toti userii cu care NU este prieten un user
     * @param forUser
     * @return
     */
    public Iterable<PrietenDTO> getPeople(String forUser){
        HashSet<Utilizator> useri = (HashSet<Utilizator>) getAllUtilizator();
        HashSet<PrietenDTO> rez = new HashSet<>();
        useri.stream().filter(x->
                repoPrietenie.findOne(new Tuple<String>(forUser, x.getId())) == null && !x.getId().equals(forUser) && (repoCereri.findOne(forUser, x.getId()) == null || !repoCereri.findOne(forUser, x.getId()).getStatus().equals("pending"))).forEach(a -> {
            PrietenDTO pr = new PrietenDTO(a.getId(), a.getFirstName(), a.getLastName());
            rez.add(pr);
        });
        return rez;
    }

    /**
     * Returneaza toate cererile trimise de un user
     * @param fromUser
     * @return
     */
    public Iterable<PrietenDTO> getCereriTrimise(String fromUser){
        HashSet<CererePrietenie> cereri = (HashSet<CererePrietenie>) repoCereri.findReqFromOne(fromUser);
        HashSet<PrietenDTO> rez = new HashSet<>();
        cereri.forEach(x->{
            Utilizator user = repoUser.findOne(x.getToUser());
            PrietenDTO pr = new PrietenDTO(user.getId(), user.getFirstName(), user.getLastName());
            rez.add(pr);
        });
        return rez;
    }



    //========================================================================== MESSAGE REPOS


    /**
     * Trimite un mesaj privat, catre un user
     * @param fromUser
     * @param toUser
     * @param message
     */
    public void sendPrivateMessage(String fromUser, String toUser, String message){
        Message msg = new Message(fromUser, toUser, message, -1);
        repoMessage.save(msg);
    }

    /**
     * Trimite un mesaj intr-un grup
     * @param fromUser
     * @param groupId
     * @param message
     */
    public void sendGroupMessage(String fromUser, int groupId, String message){
        Message msg = new Message(fromUser, null, message, groupId);
        repoMessage.save(msg);
    }

    /**
     * Adauga un user intr-un grup
     * @param addedUser
     * @param Group
     */
    public void addToGroup(String addedUser, int Group){
        if (repoGrup.isInGroup(addedUser, Group)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eroare!");
            alert.setHeaderText(null);
            alert.setContentText("Acest utilizator este deja in grup");

            alert.showAndWait();
        }
        else {
            repoGrup.addToGroup(addedUser, Group);
        }
    }

    /**
     * Creeaza un grup nou
     * @param user
     * @param name
     */
    public void createGroup(String user, String name){
        Grup grup = new Grup(0, name);
        int id = repoGrup.createGroup(grup);
        repoGrup.addToGroup(user, id);
    }

    /**
     * Returneaza mesajele dintre 2 useri
     * @param user1
     * @param user2
     * @return
     */
    public Iterable<Message> getPrivateChat(String user1, String user2){
        return repoMessage.findUsersChat(user1, user2);
    }

    /**
     * Returneaza mesajele dintr-un grup
     * @param groupId
     * @return
     */
    public Iterable<Message> getGroupChat(int groupId){
        return repoMessage.findGroupChat(groupId);

    }

    /**
     * Returneaza toate grupurile din care face parte un user
     * @param user
     * @return
     */
    public Iterable<GrupDTO> getGroupsForUser(String user){
        return repoGrup.findGroupsUser(user);
    }

    public LocalDateTime getLastMessageDate(String user1, String user2){ return repoMessage.getLastMessageDate(user1, user2);}



    //========================================================================== EVENT



    /**
     * Returneaza toate evenimentele la care participa un user
     * @param user
     * @return
     */
    public Iterable<Eveniment> getEventsForUser(String user){ return repoEvent.findEventsUser(user); }

    /**
     * Returneaza toate evenimentele la care NU participa un user
     * @param user
     * @return
     */
    public Iterable<Eveniment> getEvents(String user){
        Collection<Eveniment> evenimente = (Collection<Eveniment>) repoEvent.findAll();
        Collection<Eveniment> rez = new ArrayList<>();
        evenimente.stream().filter(x->
                repoEvent.findOne(user, x.getId()) == null).forEach(a -> {
            Eveniment ev = new Eveniment(a.getId(), a.getNume(), a.getLocatie(), a.getData(), 1);
            rez.add(ev);
        });
        return rez;
    }

    /**
     * Creeaza un eveniment nou
     * @param nume
     * @param locatie
     * @param data
     */
    public void createEvent(String nume, String locatie, LocalDate data){
        repoEvent.createEvent(new Eveniment(0, nume, locatie, data, 1));
    }

    /**
     * Adauga un user la un eveniment
     * @param user
     * @param id
     */
    public void joinEvent(String user, int id){
        repoEvent.addToEvent(user, id);
    }

    /**
     * Elimina un user dintr-un eveniment
     * @param user
     * @param id
     */
    public void leaveEvent(String user, int id){
        repoEvent.leaveEvent(user, id);
    }

    public void notifyEvents(String user){
        List<Eveniment> evNotif = (List<Eveniment>) repoEvent.getUpcomingEvents(user);

        if (evNotif.size() > 0){
            String mesaj = "Se apropie " + evNotif.size() + " evenimente: \n";

            for (Eveniment e:evNotif)
                mesaj = mesaj + e.getNume() + " " + e.getData().toString() + "\n";


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notificare");
            alert.setHeaderText(null);
            alert.setContentText(mesaj);

            alert.showAndWait();
        }
    }

    public void changeNotification(String user, int idEvent, int notif){
        repoEvent.changeNotif(user,  notif, idEvent);
    }

    public void deleteOldEvents(){
        repoEvent.deleteOldEvents();
    }

    public int getParticipants(int idEvent){
        return repoEvent.getParticipantCount(idEvent);
    }



    //========================================================================== PAGE


    /**
     * Creeaza un obiect Page pentru un user
     * @param email
     * @return
     */
    public Page getPage(String email) {
        Utilizator user = repoUser.findOne(email);
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Collection<PrietenDTO> prieteni = (Collection<PrietenDTO>) getAllPrieteniUser(email);
        List<Message> mesaje = new ArrayList<>();
        HashSet<CereriDTO> cereri = (HashSet<CereriDTO>) getAllCereri(email);

        Page pagina = new Page(email, lastName, firstName, prieteni, mesaje, cereri);
        return pagina;
    }


    //========================================================================== RAPOARTE

    public void format(PDPageContentStream content) throws IOException {
        content.setFont(PDType1Font.COURIER, 12) ;
        content.setLeading(10f);
        content.newLineAtOffset(20, 700);

    }

    public PDDocument generateRaportActivity(String email, LocalDate begin, LocalDate end ){
        try (PDDocument doc = new PDDocument()){
            List<Message> msgs = (List<Message>) repoMessage.findReceivedMsg(email,begin,end);
            int x=0;
            int step=10;
            while(x<msgs.size()){
                int fin = x+step;
                if(x+step>msgs.size()) {
                    fin=msgs.size();
                }

                List<Message> curent=msgs.subList(x,fin);
                PDPage page =  new PDPage();
                doc.addPage(page);
                try(PDPageContentStream stream = new PDPageContentStream(doc,page)){
                    stream.beginText();
                    format(stream);
                    stream.showText("MESAJE PRIMITE");
                    stream.newLine();
                    stream.newLine();
                    for(Message m : curent){
                        stream.showText(m.getFrom());
                        stream.newLine();
                        stream.showText(m.getText());
                        stream.newLine();
                        stream.showText(m.getData().format(Constants.DATE_TIME_FORMATTER).toString());
                        stream.newLine();
                        stream.newLine();
                    }
                    stream.endText();

                }
                x=x+step;
            }

            List<Prietenie> pr = (List<Prietenie>) repoPrietenie.findFriendsDate(email,begin,end);
            int y=0;
            int stepy=2;

            while(y<pr.size()) {
                int fin = y + stepy;
                if (y + stepy > pr.size()) {
                    fin = pr.size();
                }

                List<Prietenie> curentPr = pr.subList(y, fin);
                PDPage pagepr = new PDPage();
                doc.addPage(pagepr);
                try (PDPageContentStream streamPr = new PDPageContentStream(doc, pagepr)) {
                    streamPr.beginText();
                    format(streamPr);

                    streamPr.showText("PRIETENI NOI");
                    streamPr.newLine();
                    streamPr.newLine();
                    for (Prietenie p : curentPr) {
                        if (p.getId().getRight().equals(email)) {
                            streamPr.showText(p.getId().getLeft());
                        } else {
                            streamPr.showText(p.getId().getRight());
                        }
                        streamPr.newLine();
                        streamPr.showText(p.getDate().toString());
                        streamPr.newLine();
                        streamPr.newLine();
                    }
                    streamPr.endText();
                }
                y=y+stepy;
            }
            doc.save("activity_" + email + ".pdf");

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public PDDocument generateRaportMessages(String email, String from, LocalDate begin, LocalDate end ){
        try (PDDocument doc = new PDDocument()){
            List<Message> msgs = (List<Message>) repoMessage.findReceivedMsgFromUser(email, from, begin, end);
            int x=0;
            int step=10;
            while(x<msgs.size()){
                int fin = x+step;
                if(x+step>msgs.size()) {
                    fin=msgs.size();
                }

                List<Message> curent=msgs.subList(x,fin);
                PDPage page =  new PDPage();
                doc.addPage(page);
                try(PDPageContentStream stream = new PDPageContentStream(doc,page)){
                    stream.beginText();
                    format(stream);
                    stream.showText("MESAJE PRIMITE");
                    stream.newLine();
                    stream.newLine();
                    for(Message m : curent){
                        stream.showText(m.getFrom());
                        stream.newLine();
                        stream.showText(m.getText());
                        stream.newLine();
                        stream.showText(m.getData().format(Constants.DATE_TIME_FORMATTER).toString());
                        stream.newLine();
                        stream.newLine();
                    }
                    stream.endText();

                }
                x=x+step;
            }

            doc.save("messagesFrom_" + from + "_To_" + email + ".pdf");

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
