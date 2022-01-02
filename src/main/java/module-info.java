module com.example.network5 {
    requires javafx.controls;
    requires javafx.fxml;

        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens domain to javafx.base;

    opens com.example.network5 to javafx.fxml;


    exports com.example.network5;
    opens utils to javafx.base;


}