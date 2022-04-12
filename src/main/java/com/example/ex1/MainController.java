package com.example.ex1;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.w3c.dom.Text;
import socialnetwork.domain.*;
import socialnetwork.service.Service;
import socialnetwork.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController {
    private int from = 0;
    private int to = 0;
    private int itemPerPage = 5;

    private int fromUserIndex = 0;
    private int toUserIndex = 0;
    private int itemPerPageUser = 5;

    Service srv;
    Page page;
    HashSet<PrietenDTO> people;
    private PrietenDTO selectedMsgFriend;
    private GrupDTO selectedGroup;
    int switchChat = 1;

    private Collection<Eveniment> events;

    @FXML
    private Label accName;

    @FXML
    private Pane friendPane;

    @FXML
    private Pane msgPane;

    @FXML
    private Pane eventPane;

    @FXML
    private Button BtnPrieteni;

    private ObservableList<CereriDTO> modelRequests = FXCollections.observableArrayList();

    @FXML
    private TableView<CereriDTO> reqTable;
    @FXML
    private TableColumn<CereriDTO, String> reqNumeColumn;
    @FXML
    private TableColumn<CereriDTO, String> reqPrenumeColumn;
    @FXML
    private TableColumn<CereriDTO, String> reqStatusColumn;
    @FXML
    private TableColumn<CereriDTO, LocalDate> reqDataColumn;
    @FXML
    private TableColumn<CereriDTO, LocalDate> reqEmailColumn;


    private ObservableList<PrietenDTO> modelFriends = FXCollections.observableArrayList();

    @FXML
    private TableView<PrietenDTO> friendTable;
    @FXML
    private TableColumn<PrietenDTO, String> friendNumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> friendPrenumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> friendEmailColumn;

    @FXML
    private TextField searchFriendsBar;

    private ObservableList<PrietenDTO> modelSearch = FXCollections.observableArrayList();

    @FXML
    private TableView<PrietenDTO> searchPeopleTable;
    @FXML
    private TableColumn<PrietenDTO, String> searchNumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> searchPrenumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> searchEmailColumn;

    @FXML
    private TextField searchPeopleBar;

    private ObservableList<PrietenDTO> modelSentRequests = FXCollections.observableArrayList();

    @FXML
    private TableView<PrietenDTO> sentRequestsTable;
    @FXML
    private TableColumn<PrietenDTO, String> sentReqNumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> sentReqPrenumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> sentReqEmailColumn;

    @FXML
    private ImageView prieteniMain;

    @FXML
    private ImageView chatMain;

    @FXML
    private ImageView eventsMain;

    @FXML
    private ImageView logOutMain;

    private ObservableList<PrietenDTO> modelMsgFriends = FXCollections.observableArrayList();

    @FXML
    private TableView<PrietenDTO> msgFriendsTable;
    @FXML
    private TableColumn<PrietenDTO, String> msgNumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> msgPrenumeColumn;
    @FXML
    private TableColumn<PrietenDTO, String> msgEmailColumn;
    @FXML
    private TableColumn<PrietenDTO, Integer> selectedFriendColumn;
    @FXML
    private TableColumn<PrietenDTO, LocalDateTime> msgLastMsgColumn;

    private ObservableList<GrupDTO> modelMsgGroup = FXCollections.observableArrayList();

    @FXML
    private TableView<GrupDTO> msgGroupTable;
    @FXML
    private TableColumn<GrupDTO, String> msgGroupColumn;
    @FXML
    private TableColumn<GrupDTO, Integer> msgGroupIdColumn;
    @FXML
    private TableColumn<GrupDTO, Integer> selectedGroupColumn;

    private ObservableList<Eveniment> modelUserEvent = FXCollections.observableArrayList();

    @FXML
    private TableView<Eveniment> userEventsTable;
    @FXML
    private TableColumn<Eveniment, String> userEventNameColumn;
    @FXML
    private TableColumn<Eveniment, String> userEventPlaceColumn;
    @FXML
    private TableColumn<Eveniment, Date> userEventDateColumn;
    @FXML
    private TableColumn<Eveniment, Integer> userEventIdColumn;

    private ObservableList<Eveniment> modelEvent = FXCollections.observableArrayList();

    @FXML
    private TableView<Eveniment> eventsTable;
    @FXML
    private TableColumn<Eveniment, String> eventNameColumn;
    @FXML
    private TableColumn<Eveniment, String> eventPlaceColumn;
    @FXML
    private TableColumn<Eveniment, Date> eventDateColumn;
    @FXML
    private TableColumn<Eveniment, Integer> eventIdColumn;


    @FXML
    private TextField groupNameBar;

    @FXML
    private ListView<String> chatWindow;

    @FXML
    private TextField chatBar;


    @FXML
    private TextField eventNameBar;

    @FXML
    private TextField eventPlaceBar;

    @FXML
    private DatePicker eventDateBar;

    @FXML
    private TextField searchEventsBar;

    @FXML
    private Label eventWarningLabel;

    @FXML
    public Pane headerPane;

    @FXML
    public Button minimize;

    @FXML
    public Button exit;

    @FXML
    public ImageView logoUp;

    @FXML
    public DatePicker startDateBar;

    @FXML
    public  DatePicker endDateBar;

    @FXML
    public TextField friend;

    @FXML
    public Pane frPane;

    @FXML
    public Pane reqPane;

    @FXML
    public Button BtnFriends;

    @FXML
    public Button BtnRequsts;

    @FXML
    public ImageView search;

    @FXML
    public Label searchLabel;

    @FXML
    public ImageView clockImg;

    @FXML
    public ImageView deliveredImg;

    @FXML
    public Label waitingLabel;

    @FXML
    public Label deliveredLabel;

    @FXML
    public Label notifLabel;

    @FXML
    public Button notifButton;

    @FXML
    public  ImageView notifOn;

    @FXML
    public ImageView notfiOff;

    @FXML
    public Label notifLabelOn;

    @FXML
    public Label notifLabelOff;

    @FXML
    public Label numeEveniment;

    @FXML Label nrParticipantiEveniment;

    @FXML
    public Label dataEveniment;

    @FXML
    public Label numeLocatieEveniment;

    @FXML
    public ImageView numeEvenimentImg;

    @FXML ImageView nrParticipantiEvenimentImg;

    @FXML
    public ImageView dataEvenimentImg;

    @FXML
    public ImageView numeLocatieEvenimentImg;

    @FXML
    public Separator sepEv_1;

    @FXML
    public Separator sepEv_2;

    @FXML
    public Separator sepEv_3;

    @FXML
    public Label raportLabel;

    @FXML
    public ImageView raportImg;

    @FXML
    public Pagination pageBar;

    @FXML
    public Pagination userEventPagination;

    @FXML
    public TextField searchMsgFriendsBar;

    @FXML
    public Label yourEvents;

    @FXML
    public ImageView yourEventsImg;

    @FXML
    public ImageView addImg;

    @FXML
    public Button addToGroupBtn;


    public void initialize(){
        openEvents();

        reqNumeColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        reqPrenumeColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        reqStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reqDataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        reqEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        reqTable.setItems(modelRequests);

        friendNumeColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendPrenumeColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        friendTable.setItems(modelFriends);


        searchFriendsBar.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFriends();
        });



        searchNumeColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        searchPrenumeColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        searchEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        searchPeopleBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()){
                searchPeople();
            }
            else{
                modelSearch.clear();
            }
        });

        searchPeopleTable.setItems(modelSearch);


        sentReqNumeColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        sentReqPrenumeColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        sentReqEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        sentRequestsTable.setItems(modelSentRequests);

        File filePrMain = new File("resurse/prieteni.png");
        Image  imgPrMaine= new Image(filePrMain.toURI().toString());
        prieteniMain.setImage(imgPrMaine);

        File fileChMain = new File("resurse/mesaje.png");
        Image imgChMain = new Image(fileChMain.toURI().toString());
        chatMain.setImage(imgChMain);

        File fileEvMain = new File("resurse/events2.png");
        Image imgEvMain = new Image(fileEvMain.toURI().toString());
        eventsMain.setImage(imgEvMain);

        File fileLgMain = new File("resurse/logout.png");
        Image imgLgMain = new Image(fileLgMain.toURI().toString());
        logOutMain.setImage(imgLgMain);

        File fileAddMain = new File("resurse/add_.png");
        Image imgAddMain = new Image(fileAddMain.toURI().toString());
        ImageView imgViewAdd = new ImageView(imgAddMain);
        addToGroupBtn.setGraphic(imgViewAdd);


        File fileYourEvMain = new File("resurse/yourEvents.png");
        Image imgYourEv = new Image(fileYourEvMain.toURI().toString());
        ImageView imgViewYourEv = new ImageView(imgYourEv);
        yourEvents.setGraphic(imgViewYourEv);

        File fileNameEvMain = new File("resurse/nameEv.png");
        Image imgNameEv = new Image(fileNameEvMain.toURI().toString());
        ImageView imgViewNameEv = new ImageView(imgNameEv);
        numeEveniment.setGraphic(imgViewNameEv);

        File fileRaportMain = new File("resurse/raport.png");
        Image imgRaport = new Image(fileRaportMain.toURI().toString());
        ImageView imgVRaport = new ImageView(imgRaport);
        raportLabel.setGraphic(imgVRaport);

        File fileDateEvMain = new File("resurse/dateEv.png");
        Image imgDateEv = new Image(fileDateEvMain.toURI().toString());
        ImageView imgViewDateEv = new ImageView(imgDateEv);
        dataEveniment.setGraphic(imgViewDateEv);

        File fileLocEvMain = new File("resurse/locationEv.png");
        Image imgLocEv = new Image(fileLocEvMain.toURI().toString());
        ImageView imgViewLocEv = new ImageView(imgLocEv);
        numeLocatieEveniment.setGraphic(imgViewLocEv);

        File fileAtEvMain = new File("resurse/attendensEv.png");
        Image imgAtEv = new Image(fileAtEvMain.toURI().toString());
        ImageView imgViewAtEv = new ImageView(imgAtEv);
        nrParticipantiEveniment.setGraphic(imgViewAtEv);


        File fileNotOnMain = new File("resurse/notOn.png");
        Image imgNotOn = new Image(fileNotOnMain.toURI().toString());
        ImageView imgViewNotOn = new ImageView(imgNotOn);
        notifLabelOn.setGraphic(imgViewNotOn);

        File fileNotOffMain = new File("resurse/notOff.png");
        Image imgNotOff = new Image(fileNotOffMain.toURI().toString());
        ImageView imgViewNotOff = new ImageView(imgNotOff);
        notifLabelOff.setGraphic(imgViewNotOff);

        File fileAccMain = new File("resurse/user.png");
        Image imgAcc = new Image(fileAccMain.toURI().toString());
        ImageView imgViewAcc = new ImageView(imgAcc);
        accName.setGraphic(imgViewAcc);

        File fileSearchMain = new File("resurse/search.png");
        Image imgSearch = new Image(fileSearchMain.toURI().toString());
        ImageView imgSearchAcc = new ImageView(imgSearch);
        searchLabel.setGraphic(imgSearchAcc);

        File fileWaitingMain = new File("resurse/clock.png");
        Image imgWaiting = new Image(fileWaitingMain.toURI().toString());
        ImageView imgWaitingAcc = new ImageView(imgWaiting);
        waitingLabel.setGraphic(imgWaitingAcc);

        File fileDeliveredMain = new File("resurse/delivered.png");
        Image imgDelivered = new Image(fileDeliveredMain.toURI().toString());
        ImageView imgDeliveredAcc = new ImageView(imgDelivered);
        deliveredLabel.setGraphic(imgDeliveredAcc);

        File fileLogoUpFile = new File("resurse/SocialULogo.png");
        Image imgLogoUp = new Image(fileLogoUpFile.toURI().toString());
        logoUp.setImage(imgLogoUp);




        Label cerereriLabel = new Label("No pending requests");
        cerereriLabel.setTextFill(Color.web("#ffffff"));
        reqTable.setPlaceholder(cerereriLabel);

        reqTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        friendTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        searchPeopleTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        sentRequestsTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        msgFriendsTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        msgGroupTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        Label prieteniLabel = new Label("No friends");
        prieteniLabel.setTextFill(Color.web("#ffffff"));
        friendTable.setPlaceholder(prieteniLabel);

        Label utilizatoriLabel = new Label("Search for new friends...");
        utilizatoriLabel.setTextFill(Color.web("#ffffff"));
        searchPeopleTable.setPlaceholder(utilizatoriLabel);

        Label trimisaLabel = new Label("No sent requests");
        trimisaLabel.setTextFill(Color.web("#ffffff"));
        sentRequestsTable.setPlaceholder(trimisaLabel);

        Label evenimentTotal = new Label("No events to attend");
        evenimentTotal.setTextFill(Color.web("#ffffff"));
        eventsTable.setPlaceholder(evenimentTotal);

        Label evenimentParticip = new Label("Not attending any events");
        evenimentParticip.setTextFill(Color.web("#ffffff"));
        userEventsTable.setPlaceholder(evenimentParticip);


        msgNumeColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        msgPrenumeColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        msgEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        selectedFriendColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        msgLastMsgColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        msgFriendsTable.setItems(modelMsgFriends);

        msgGroupColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        msgGroupIdColumn.setCellValueFactory(new PropertyValueFactory<>("groupId"));
        selectedGroupColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));

        msgGroupTable.setItems(modelMsgGroup);

        msgFriendsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                int index = msgFriendsTable.getItems().indexOf(selectedMsgFriend);
                if (index!=-1) {
                    selectedMsgFriend.setSelected("");
                    msgFriendsTable.getItems().set(index, selectedMsgFriend);
                }

                selectedMsgFriend = msgFriendsTable.getSelectionModel().getSelectedItem();

                index = msgFriendsTable.getItems().indexOf(selectedMsgFriend);
                selectedMsgFriend.setSelected(">");
                msgFriendsTable.getItems().set(index, selectedMsgFriend);

                switchChat = 2;
                loadChat();
                msgGroupTable.getSelectionModel().select(null);
            }
        });

        msgGroupTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                int index = msgGroupTable.getItems().indexOf(selectedGroup);
                if (index!=-1) {
                    selectedGroup.setSelected("");
                    msgGroupTable.getItems().set(index, selectedGroup);
                }

                selectedGroup = msgGroupTable.getSelectionModel().getSelectedItem();

                index = msgGroupTable.getItems().indexOf(selectedGroup);
                selectedGroup.setSelected(">");
                msgGroupTable.getItems().set(index, selectedGroup);

                switchChat = 3;
                loadChat();
                msgFriendsTable.getSelectionModel().select(null);
            }
        });

        userEventNameColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        userEventPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("locatie"));
        userEventDateColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        userEventIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        userEventsTable.setItems(modelUserEvent);
        userEventsTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        eventPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("locatie"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        eventsTable.setItems(modelEvent);
        eventsTable.getStylesheets().add(getClass().getResource("/style/table.css").toExternalForm());

        eventDateBar.setEditable(false);

        searchEventsBar.textProperty().addListener((observable, oldValue, newValue) -> {
            searchEvents();
            int count = modelEvent.size();
            int pageCount = (count/itemPerPage) +1;
            pageBar.setPageCount(pageCount);
            pageBar.setPageFactory(this::createPage);
        });

        friendTable.setOnMouseClicked(event->{
            //friendTable.getSelectionModel().getSelectedItem().getEmail();
            friend.setText(friendTable.getSelectionModel().getSelectedItem().getEmail());
        });

        eventsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection !=null) {

                numeEveniment.setText(eventsTable.getSelectionModel().getSelectedItem().getNume());
                numeEveniment.setVisible(true);
                sepEv_1.setVisible(true);

                numeLocatieEveniment.setText(eventsTable.getSelectionModel().getSelectedItem().getLocatie());
                numeLocatieEveniment.setVisible(true);
                sepEv_2.setVisible(true);

                int part = srv.getParticipants(eventsTable.getSelectionModel().getSelectedItem().getId());
                nrParticipantiEveniment.setText(String.valueOf(part));
                nrParticipantiEveniment.setVisible(true);
                sepEv_3.setVisible(true);

                userEventsTable.getSelectionModel().select(null);

                dataEveniment.setText(eventsTable.getSelectionModel().getSelectedItem().getData().toString());
                dataEveniment.setVisible(true);
            }
        });

        userEventsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                    numeEveniment.setText(userEventsTable.getSelectionModel().getSelectedItem().getNume());
                    numeEveniment.setVisible(true);
                    sepEv_1.setVisible(true);

                    numeLocatieEveniment.setText(userEventsTable.getSelectionModel().getSelectedItem().getLocatie());
                    numeLocatieEveniment.setVisible(true);
                    sepEv_2.setVisible(true);

                    int part = srv.getParticipants(userEventsTable.getSelectionModel().getSelectedItem().getId());
                    nrParticipantiEveniment.setText(String.valueOf(part));
                    nrParticipantiEveniment.setVisible(true);
                    sepEv_3.setVisible(true);

                    dataEveniment.setText(userEventsTable.getSelectionModel().getSelectedItem().getData().toString());
                    dataEveniment.setVisible(true);

                    eventsTable.getSelectionModel().select(null);

                    //aici am testat daca merge count-ul de participanti
                    //System.out.println(srv.getParticipants(userEventsTable.getSelectionModel().getSelectedItem().getId()));

                if (newSelection.getNotif() == 1) {
                    notifLabelOn.setVisible(true);
                    notifLabelOff.setVisible(false);
                }
                else{
                    notifLabelOn.setVisible(false);
                    notifLabelOff.setVisible(true);
                }
            }
        });

        searchMsgFriendsBar.textProperty().addListener((observable, oldValue, newValue) -> {
            searchMessageFriends();
        });

        msgFriendsTable.getSortOrder().add(msgLastMsgColumn);
    }

    public void searchMessageFriends(){
        String substring = searchMsgFriendsBar.getText();

        Collection<PrietenDTO> prieteni =  page.getPrieteni();
        modelMsgFriends.clear();
        for (PrietenDTO p:prieteni){
            if (((p.getLastName() + " " + p.getFirstName()).toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT))) ||
                    ((p.getFirstName() + " " + p.getLastName()).toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT)))){

                modelMsgFriends.add(p);
            }
        }
    }

    public void setUserService(Service service,String email) {
        this.srv=service;
        page = srv.getPage(email);

        srv.deleteOldEvents();
        loadTableRequests();
        loadTableFriends();
        loadTableSentRequests();
        loadTableMsgFriends();
        loadGroupTable();
        loadUserEvents();

        accName.setText(page.getNume() + " " + page.getPrenume());
        people = (HashSet<PrietenDTO>) srv.getPeople(page.getEmail());
        events = (Collection<Eveniment>) srv.getEvents(page.getEmail());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                srv.notifyEvents(page.getEmail());
            }
        });

        for (Eveniment e : events)
            modelEvent.add(e);

        int count = modelEvent.size();
        int pageCount = (count/itemPerPage) +1;
        pageBar.setPageCount(pageCount);
        pageBar.setPageFactory(this::createPage);
    }

    public void loadUserEvents(){
        modelUserEvent.setAll((Collection<? extends Eveniment>) srv.getEventsForUser(page.getEmail()));

        int count = modelUserEvent.size();
        int pageCount = (count/itemPerPageUser) +1;
        userEventPagination.setPageCount(pageCount);
        userEventPagination.setPageFactory(this::createPageUser);
    }

    public void loadChat(){

        chatWindow.getItems().clear();

        if (switchChat == 2) {
            ArrayList<Message> msgs = (ArrayList<Message>) srv.getPrivateChat(page.getEmail(), selectedMsgFriend.getEmail());
            for (Message m : msgs) {
                chatWindow.getItems().add(m.getFrom() + "\n" + m.getText() + "\n" + m.getData().format(Constants.DATE_TIME_FORMATTER) + "\n");
                chatWindow.getItems().add("");
            }
            chatWindow.scrollTo(chatWindow.getItems().size());
        }
        else if (switchChat == 3){
            ArrayList<Message> msgs = (ArrayList<Message>) srv.getGroupChat(selectedGroup.getGroupId());
            for (Message m : msgs) {
                chatWindow.getItems().add(m.getFrom() + "\n" + m.getText() + "\n" + m.getData().format(Constants.DATE_TIME_FORMATTER) + "\n");
                chatWindow.getItems().add("");
            }
            chatWindow.scrollTo(chatWindow.getItems().size());
        }
    }

    public void sendMessage(){
        String mesaj = chatBar.getText();

        if (!mesaj.equals("")) {
            if (switchChat == 2) {
                srv.sendPrivateMessage(page.getEmail(), selectedMsgFriend.getEmail(), mesaj);
                chatBar.setText("");

            } else if (switchChat == 3) {
                srv.sendGroupMessage(page.getEmail(), selectedGroup.getGroupId(), mesaj);
                chatBar.setText("");
            }

        }
        loadChat();

        if (!mesaj.equals(""))
            if (switchChat == 2) {
                page.setPrieteni((Collection<PrietenDTO>) srv.getAllPrieteniUser(page.getEmail()));
                String id = selectedMsgFriend.getEmail();
                loadTableMsgFriends();
                for (PrietenDTO pr : msgFriendsTable.getItems())
                    if (pr.getEmail().equals(id)) {
                        selectedMsgFriend = pr;
                    }
                int index = msgFriendsTable.getItems().indexOf(selectedMsgFriend);
                selectedMsgFriend.setSelected(">");
                msgFriendsTable.getItems().set(index, selectedMsgFriend);
            }
    }

    public void loadTableMsgFriends(){
        Collection<PrietenDTO> pr = page.getPrieteni();
        modelMsgFriends.setAll(pr);
        selectedMsgFriend = null;
    }

    public void loadTableRequests(){
        modelRequests.setAll((Collection<? extends CereriDTO>) srv.getAllCereri(page.getEmail()));
    }

    public void loadGroupTable(){
        modelMsgGroup.setAll((Collection<? extends GrupDTO>) srv.getGroupsForUser(page.getEmail()));
        selectedGroup = null;
    }

    public void addToGroup(){
        if (selectedGroup != null && selectedMsgFriend != null){
            srv.addToGroup(selectedMsgFriend.getEmail(), selectedGroup.getGroupId());
        }
    }

    public void logOut() throws IOException {
        Main main = new Main();
        main.switchToLogIn("login.fxml");
    }

    public void acceptRequest(){
        CereriDTO cerere = reqTable.getSelectionModel().getSelectedItem();

        if (cerere != null) {
            srv.updateCereri(cerere.getEmail(), page.getEmail(), "approved");
            page.setPrieteni((Collection<PrietenDTO>) srv.getAllPrieteniUser(page.getEmail()));
            loadTableRequests();
            loadTableFriends();
            loadTableMsgFriends();
            chatWindow.getItems().clear();
        }
    }

    public void createGroup(){
        String groupName = groupNameBar.getText();

        if (!groupName.equals("")){
            srv.createGroup(page.getEmail(), groupName);
            loadGroupTable();
        }
    }

    public void declineRequest(){
        CereriDTO cerere = reqTable.getSelectionModel().getSelectedItem();

        if (cerere != null) {
            srv.updateCereri(cerere.getEmail(), page.getEmail(), "rejected");
            loadTableRequests();
            people = (HashSet<PrietenDTO>) srv.getPeople(page.getEmail());
            searchPeople();
        }
    }

    public void loadTableFriends(){
        modelFriends.setAll(page.getPrieteni());
    }

    public void loadTableSentRequests(){
        modelSentRequests.setAll((Collection<? extends PrietenDTO>) srv.getCereriTrimise(page.getEmail()));
    }


    public void deleteFriend(){
        PrietenDTO prieten = friendTable.getSelectionModel().getSelectedItem();

        if (prieten != null){
            srv.delPrietenie(page.getEmail(), prieten.getEmail());
            page.setPrieteni((Collection<PrietenDTO>) srv.getAllPrieteniUser(page.getEmail()));
            loadTableFriends();
            loadTableMsgFriends();
            people = (HashSet<PrietenDTO>) srv.getPeople(page.getEmail());
            searchPeople();
            chatWindow.getItems().clear();
        }
    }

    public void searchFriends(){
        String substring = searchFriendsBar.getText();

        Collection<PrietenDTO> prieteni =  page.getPrieteni();
        modelFriends.clear();
        for (PrietenDTO p:prieteni){
            if (((p.getLastName() + " " + p.getFirstName()).toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT))) ||
                    ((p.getFirstName() + " " + p.getLastName()).toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT)))){

                modelFriends.add(p);
            }
        }
    }

    public void searchPeople(){
        String substring = searchPeopleBar.getText();
        modelSearch.clear();

        if (!substring.equals("")) {
            for (PrietenDTO p : people) {
                if (((p.getLastName() + " " + p.getFirstName()).toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT))) ||
                        ((p.getFirstName() + " " + p.getLastName()).toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT)))) {

                    modelSearch.add(p);
                }
            }
        }
    }

    /**
     * Cauta in evenimentele la care user-ul nu participa (folosit la search bar)
     */
    public void searchEvents(){
        String substring = searchEventsBar.getText();
        modelEvent.clear();

        for (Eveniment e : events){
            if( e.getNume().toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT))){
                modelEvent.add(e);
            }
        }
    }

    /**
     * Creeaza un eveniment nou
     */
    public void createEvent(){
        String nume = eventNameBar.getText();
        String locatie = eventPlaceBar.getText();
        LocalDate data = eventDateBar.getValue();

        if (!nume.equals("") && !locatie.equals("") && data != null) {
            eventWarningLabel.setVisible(false);
            srv.createEvent(nume, locatie, data);
            events = (Collection<Eveniment>) srv.getEvents(page.getEmail());
            searchEvents();

            eventNameBar.setText("");
            eventPlaceBar.setText("");
            eventDateBar.setValue(null);

            int count = modelEvent.size();
            int pageCount = (count/itemPerPage) +1;
            pageBar.setPageCount(pageCount);
            pageBar.setPageFactory(this::createPage);
        }
        else{
            eventWarningLabel.setVisible(true);
        }
    }

    /**
     * Aboneaza la un eveniment
     */
    public void joinEvent(){
        Eveniment eveniment = eventsTable.getSelectionModel().getSelectedItem();

        if (eveniment != null){
            srv.joinEvent(page.getEmail(), eveniment.getId());
            loadUserEvents();
            events = (Collection<Eveniment>) srv.getEvents(page.getEmail());
            searchEvents();

            int count = modelEvent.size();
            int pageCount = (count/itemPerPage) +1;
            pageBar.setPageCount(pageCount);
            pageBar.setPageFactory(this::createPage);
        }
    }

    /**
     * Paraseste un eveniment
     */
    public void leaveEvent(){
        Eveniment eveniment = userEventsTable.getSelectionModel().getSelectedItem();

        if (eveniment != null){
            srv.leaveEvent(page.getEmail(), eveniment.getId());
            loadUserEvents();
            events = (Collection<Eveniment>) srv.getEvents(page.getEmail());
            searchEvents();

            int count = modelEvent.size();
            int pageCount = (count/itemPerPage) +1;
            pageBar.setPageCount(pageCount);
            pageBar.setPageFactory(this::createPage);
        }
    }

    public void deleteSentRequest(){
        PrietenDTO pr = sentRequestsTable.getSelectionModel().getSelectedItem();

        if (pr != null) {
            srv.updateCereri(page.getEmail(), pr.getEmail(), "rejected");
            loadTableSentRequests();
            people = (HashSet<PrietenDTO>) srv.getPeople(page.getEmail());
            searchPeople();
        }
    }

    public void sendFriendRequest(){
        PrietenDTO pr = searchPeopleTable.getSelectionModel().getSelectedItem();

        if (pr!=null){
            srv.addCerere(page.getEmail(), pr.getEmail());
            people = (HashSet<PrietenDTO>) srv.getPeople(page.getEmail());
            searchPeople();
            loadTableSentRequests();
        }
    }

    /**
     * WORK IN PROGRESS
     * Reincarca tot main page-ul
     */
    public void refresh(){
        loadTableRequests();
        loadTableFriends();
        loadTableSentRequests();
        loadTableMsgFriends();
        loadGroupTable();
        loadUserEvents();
        chatWindow.getItems().clear();

        accName.setText(page.getNume() + " " + page.getPrenume());
        people = (HashSet<PrietenDTO>) srv.getPeople(page.getEmail());
        events = (HashSet<Eveniment>) srv.getEvents(page.getEmail());

        eventNameBar.setText("");
        eventPlaceBar.setText("");
        eventDateBar.setValue(null);

        chatBar.setText("");
        searchEventsBar.setText("");
        searchPeopleBar.setText("");
        searchFriendsBar.setText("");
    }

    public  void openFriendsPane(){
        reqPane.setVisible(false);
        frPane.setVisible(true);
    }

    public  void openRequestsPane(){
        reqPane.setVisible(true);
        frPane.setVisible(false);
    }

    public void openFriends() {
        msgPane.setVisible(false);
        friendPane.setVisible(true);
        eventPane.setVisible(false);
    }

    public void openMsg() {
        friendPane.setVisible(false);
        msgPane.setVisible(true);
        eventPane.setVisible(false);
    }

    public void openEvents(){
        friendPane.setVisible(false);
        msgPane.setVisible(false);
        eventPane.setVisible(true);
    }

    public void activity(){

        LocalDate start = startDateBar.getValue();
        LocalDate end = endDateBar.getValue();
        if(start != null && end != null){
            srv.generateRaportActivity(page.getEmail(),start,end);
        }

    }

    public void messages(){
        LocalDate start = startDateBar.getValue();
        LocalDate end = endDateBar.getValue();
        if(start != null && end != null && friend.getText() != null){
            srv.generateRaportMessages(page.getEmail(), friend.getText(), start,end);
        }
    }

    public void setup(){
        PseudoClass psclass  = PseudoClass.getPseudoClass("psclass");

        friendTable.setRowFactory(table->{
            TableRow<PrietenDTO> row = new TableRow<>();
            ChangeListener<String> changeListener = ((observable, oldValue, newValue) -> {
                row.pseudoClassStateChanged(psclass, true);
            });
            return row;
        });

    }

    public void changeNotif(){
        Eveniment ev = userEventsTable.getSelectionModel().getSelectedItem();

        if (ev!=null) {
            if (ev.getNotif() == 0){
                srv.changeNotification(page.getEmail(), ev.getId(), 1);
                notifLabelOn.setVisible(true);
                notifLabelOff.setVisible(false);
                userEventsTable.getSelectionModel().getSelectedItem().setNotif(1);
            }
            else{
                srv.changeNotification(page.getEmail(), ev.getId(), 0);
                notifLabelOff.setVisible(true);
                notifLabelOn.setVisible(false);
                userEventsTable.getSelectionModel().getSelectedItem().setNotif(0);
            }
        }
    }

    private Node createPage(int pageIndex){
        from=pageIndex*itemPerPage;
        to=Math.min(from+itemPerPage,modelEvent.size());
        try {
            eventsTable.setItems(FXCollections.observableArrayList(modelEvent.subList(from,to)));
        }
        catch (Exception ex){}
        return eventsTable;
    }

    private Node createPageUser(int pageIndex){
        fromUserIndex=pageIndex*itemPerPageUser;
        toUserIndex=Math.min(fromUserIndex+itemPerPageUser,modelUserEvent.size());
        try {
            userEventsTable.setItems(FXCollections.observableArrayList(modelUserEvent.subList(fromUserIndex,toUserIndex)));
        }
        catch (Exception ex){}
        return userEventsTable;
    }
}

