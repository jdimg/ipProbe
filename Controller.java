package aufgaben1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;

public class Controller {
	
//	private Lookup2 main;
	@FXML private Label nameLabel;
	@FXML private Button ipButton;
	@FXML private Button pingButton;
	@FXML private Button pasteButton;
	@FXML private Button copyButton;
	@FXML private Button deleteButton;
	@FXML private TextField ipTextField;
	@FXML private TextArea outputTextArea;
	@FXML private Label localhostLabel;
	private String localhostLabelError;
	private String exceptionText;
	private String pingText;
	
	//Konstruktor
	public Controller() {
		nameLabel = new Label();
		localhostLabel = new Label();
		ipButton = new Button();
		pingButton = new Button();
		ipTextField = new TextField();
		outputTextArea = new TextArea();
		exceptionText = "Etwas ist schief gegangen.\nEingabe überprüfen.";
		localhostLabelError = "localhost konnte nicht ermittelt werden.";
		pingText = "Ping: "; 
	}
	
	/**
	 * Initialisierung der Controller-Klasse. Diese Methode wird automatisch
	 * aufgerufen, nachdem die fxml-Datei geladen wurde.
	 */
	@FXML
	private void initialize() {
		try {
			localhostLabel.setText(Inet4Address.getLocalHost().toString());
		} catch (UnknownHostException e) {
			localhostLabel.setText(localhostLabelError);
			e.printStackTrace();
		}
	}
	
	@FXML
	private void deleteAllInformation() {
		ipTextField.setText("");
		outputTextArea.setText("");
	}
	
	@FXML
	private void closeProgram() {
		System.exit(0);
	}
	
	public void enterPressed() {
		ipButtondHandle();
	}
	
	@FXML
	private void pasteButtonHandle() {
		String str = "";
		try {
			str = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch(HeadlessException e) {
			outputTextArea.setText("HeadlessException");
		} catch(UnsupportedFlavorException e) {
			outputTextArea.setText("UnsupportedFlavorException");
		} catch(IOException e) {
//			e.printStackTrace();
			outputTextArea.setText("IOException");
		}
		ipTextField.setText(str);
	}
	
	@FXML
	private void copyButtonHandle() {
		//TODO Wenn mehr als eine Zeile in der AusgabeArea vorhanden, soll nicht alles komplett kopiert werden.
		Toolkit.getDefaultToolkit().getSystemClipboard()
		.setContents(new StringSelection(outputTextArea.getText()), null);
	}
	
	@FXML
	private void pingButtonHandle() {
		ipPing();
	}
	
	@FXML
	private void ipButtondHandle() {
		try {
			outputTextArea.setText(inetAddressOrName(ipTextField.getText()));
		} catch(UnknownHostException e) {
			outputTextArea.setText(exceptionText + "\nFalsche Adresse?");
		} catch (NumberFormatException e) {
			outputTextArea.setText(exceptionText + " - NumberFormatException");
		} catch(ArrayIndexOutOfBoundsException e) {
//			e.printStackTrace();
			outputTextArea.setText(exceptionText + " - ArrayIndexOutOfBoundsException");
		}
	}
	
//	public void setMain(Lookup2 main) {
//		this.main = main;
//	}
	
	
	private void ipPing() {
		try {
			long finish = 0;
			long start = new GregorianCalendar().getTimeInMillis();
			
			InetAddress addr = InetAddress.getByName(ipTextField.getText());
			if(addr.isReachable(5000)) {
				finish = new GregorianCalendar().getTimeInMillis();
				outputTextArea.setText(pingText + (finish - start + " ms"));
			} else outputTextArea.setText(exceptionText);
		} catch(UnknownHostException e) {
			outputTextArea.setText(exceptionText + " - UnknownHostException");
//			e.printStackTrace();
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			outputTextArea.setText(exceptionText + " - NumberFormatException");
		} catch(IOException e) {
//			e.printStackTrace();
			outputTextArea.setText(exceptionText + " - IOException");
		}
	}

	/**
	 * Nutzt die Methoden von InetAddress, um den Hostnamen oder die IP-Adresse auszugeben, je nachdem, was man eingibt.
	 * @param strIn
	 * @throws UnknownHostException 
	 */
	private String inetAddressOrName(String strIn) throws UnknownHostException, NumberFormatException, ArrayIndexOutOfBoundsException {
		byte[] ip = new byte[4];	//IPv4 or IPv6
		if(!isNumber(strIn)) {		//ist die Eingabe eine IP-Adresse oder eine Webseiten-Adresse
			String ipString = "";
			InetAddress[] address = InetAddress.getAllByName(strIn);
			for(InetAddress tmp : address) {
				if(tmp.equals(address[address.length - 1])) {	//Gut gedacht
					ipString = ipString.concat(tmp.getHostAddress());	}
				else ipString = ipString.concat(tmp.getHostAddress() + "\n");
			}
			return ipString;
		} else {
			//TODO bei IPv4 klappt das gut, bei IPv6 gibt es : statt . (Doppelpunkte statt Punkte). Wäre noch zu implementieren. 
			String[] numbers = strIn.split("\\.");	//Warum in Klammern nicht einfach ein "." stehen kann, ist unklar.
			for(int i = 0; i < numbers.length; i++) {
				ip[i] = (byte)Integer.parseInt(numbers[i]);
			}
			InetAddress addr = InetAddress.getByAddress(ip);
			return addr.getHostName();
		}
	}
	
	/**
	 * Prüft, ob der String vor dem ersten Punkt einen Buchstaben enthält und gibt in dem Fall false aus.
	 * Anderenfalls true, falls alle Zeichen des Strings zahlen sind.
	 * @param s
	 * @return boolean
	 */
	private boolean isNumber(String s) {
		String[] inputString = s.split("\\.");		//Nur der Teilstring vor dem ersten Punkt wird berücksichtigt
		char[] lettersOrNumbers = inputString[0].toCharArray();	//der Teilstring wird in ein char-Array umgewandelt
		for(char c : lettersOrNumbers) {
			if(Character.isLetter(c)) {
				return false;		//falls der char-Array nur einen Buchstaben enthält, gilt die Eingabe nicht als IP-Adresse
			}
		}
		return true;
	}
}