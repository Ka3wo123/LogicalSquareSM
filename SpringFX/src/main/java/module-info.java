module pl.logicalsquare.springfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens pl.logicalsquare.springfx to javafx.fxml;
    exports pl.logicalsquare.springfx;
}