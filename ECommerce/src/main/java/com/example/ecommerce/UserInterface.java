package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;
    HBox headerBar;

    HBox footerBar;

    Button signInButton;

    Label welcomeLabel;

    Customer loggedInCustomer;

   ProductList productList= new ProductList();

   VBox body;

   VBox productPage;

   Button placeOrderButton= new Button("Place Order");

   ObservableList<Product> itemsInCart= FXCollections.observableArrayList();
    public BorderPane createContent(){
        BorderPane root= new BorderPane();
        root.setPrefSize(600,400);
       // root.getChildren().add(loginPage);//method to add nodes as childern  to pane
        root.setTop(headerBar);
        //root.setCenter(loginPage);
        body=new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        root.setCenter(body);
        productPage=productList.getAllProducts();
        body.getChildren().add(productPage);
      //  root.setCenter(productPage);
        root.setBottom(footerBar);

        return root;
    }
    public UserInterface(){
        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }

    private void createLoginPage(){

        Text usernametext= new Text("User Name");
        Text passwordtext= new Text("Password");

        TextField userName = new TextField();
        userName.setPromptText("Type UserName");

        PasswordField passwordField= new PasswordField();
        passwordField.setPromptText("Type Password");


        Button loginButton= new Button("Login");
        Label messageLabel= new Label("HI");

        loginPage= new GridPane();

        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(usernametext,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordtext,0,1);
        loginPage.add(passwordField,1,1);
        loginPage.add(messageLabel,0,2);
        loginPage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                String password=passwordField.getText();
                messageLabel.setText(name);
                Login login=new Login();
                loggedInCustomer= login.customerLogin(name,password);
                if(loggedInCustomer!=null){
                        messageLabel.setText("WelCome " + loggedInCustomer.getName());
                        welcomeLabel.setText("WelCome-" +loggedInCustomer.getName());
                        headerBar.getChildren().add(welcomeLabel);
                        body.getChildren().clear();
                        body.getChildren().add(productPage);
                    }
                else {
                        messageLabel.setText("Login Failed !! Please insert correct username and password");
                    }
            }

        });
    }

    private void createHeaderBar(){
        Button homeButton= new Button();
        Image image= new Image("C:\\Users\\q\\Desktop\\Acciojob_projects\\ECommerce\\src\\main\\java\\com\\example\\ecommerce\\img.jpg");
        ImageView imageView= new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(38);
        imageView.setFitWidth(90);
        homeButton.setGraphic(imageView);



        TextField searchBar= new TextField();
        searchBar.setPromptText("Search Here");
        searchBar.setPrefWidth(180);

        Button searchButton = new Button("Search");




         signInButton =new Button("Sign In");
         welcomeLabel=new Label();

         Button cartButton= new Button("Cart");

         Button orderButtton= new Button("Orders");



        headerBar= new HBox();
     //   headerBar.setStyle("-fx-background-color:grey");
        headerBar.setPadding(new Insets(10));
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER );
        headerBar.getChildren().addAll(homeButton,searchBar,searchButton,signInButton,cartButton,orderButtton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();//remove everything
                body.getChildren().add(loginPage);//put log in page
                headerBar.getChildren().remove(signInButton);
            }
        });
        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage= productList.getProductsInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);//all cases need to be handled for this
            }
        });
    placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (itemsInCart == null) {
                //please select the product first to place
                showDialog("Please add some products in the Cart to place order!");
                return;
            }
            if(loggedInCustomer ==null){
                showDialog("Please login first to place order !");
                return;
            }
            int count= Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
            if(count!=0){
                showDialog("Order for "+count+" products placed successfully !!");
            }
            else{
                showDialog("Oops..! Order failed please try again");
            }

        }
    });

    homeButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            body.getChildren().clear();
            body.getChildren().add(productPage);
            footerBar.setVisible(true);

            if (loggedInCustomer==null){
               // System.out.println(headerBar.getChildren().indexOf(signInButton));
                if(headerBar.getChildren().indexOf(signInButton)==-1)
                     headerBar.getChildren().add(signInButton);
            }

        }
    });


    }


    private void createFooterBar() {

        Button buyNowButton = new Button("Buy Now");
        Button addToCartButton= new Button("Add to Cart");


        footerBar = new HBox();

        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton,addToCartButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if (product == null) {
                    //please select the product first to place
                    showDialog("Please select the product first to place order !");
                    return;
                }
                if(loggedInCustomer ==null){
                    showDialog("Please login first to place order !");
                    return;
                }
                boolean status= Order.placeOrder(loggedInCustomer,product);
                if(status==true){
                    showDialog("Order place successfully !!");
                }
                else{
                    showDialog("Oops..! Order failed please try again");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if (product == null) {
                    //please select the product first to place
                    showDialog("Please select the product first to add to Cart!");
                    return;
                }

                itemsInCart.add(product);
                showDialog("Product added to Cart" );
            }
        });
    }
        private void showDialog(String message){
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.setTitle("Message");
            alert.showAndWait();
        }



}

