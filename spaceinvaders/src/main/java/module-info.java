open module spaceinvaders {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;

	requires java.net.http;

	requires org.slf4j;
	requires org.apache.logging.log4j;

	requires static lombok;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    exports spaceinvaders;
}
