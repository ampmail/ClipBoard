
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DataParse {
 
    public static ArrayList<Shipping> shippingData = new ArrayList<Shipping>();
    private static String lineData;
    private static String clipboardBufer;
    private static String markerStr = "Brak_ID\tдата созданияr\tТип\tsupl\tмарка\ttip_vozvr\td_otpr\tSS\tСостояние брака";

//    enum TipVozvrata = {
//                NOT_TESTED_YET ("Еще не тестировали"),
//                HAS_BEEN_TESTED ("Протестировали");
//
//        String TipVozvrataString;
//        TipVozvrata(String tipString){
//            this.TipVozvrata = tipString;
//        }
//    }

    private static JFrame msgFrame = new JFrame();

    public static boolean parseClipboard( ){

	Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
	Transferable t = c.getContents(null);
	if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	    try {
		clipboardBufer = (String)t.getTransferData(DataFlavor.stringFlavor);
	    } catch (IOException e) {
	    } catch (UnsupportedFlavorException e) {
	    }
	}
        try {
            if (clipboardBufer.length() <= markerStr.length()) {
                System.out.println("Data in clipboard buffer not match!");
                JOptionPane.showMessageDialog(msgFrame, "Data in clipboard buffer not match!");
                return false;
            }
            StringTokenizer buferTokenize = new StringTokenizer(clipboardBufer, "\n");
            if( markerStr.equals( buferTokenize.nextElement().toString() ) ) {
                FormInst.clearTableData();
                shippingData.clear();
 
                Calendar rightNow = Calendar.getInstance();               
                Date currentDate = new Date();

                while (buferTokenize.hasMoreElements()) {
                    lineData = buferTokenize.nextElement().toString();
                    String[] lineTokenize = lineData.split( "\t");

                    Integer bID;
                    Date crDate = null;
                    String supl = "";
                    String marka = "";
                    String tipVozvrata = "";
                    String sostoyanie = "";

                    bID = Integer.parseInt( lineTokenize[0] );
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        crDate = format.parse( lineTokenize[1] );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (lineTokenize.length > 3) supl = lineTokenize[3];
                    if (lineTokenize.length > 4) marka = lineTokenize[4];
                    if (lineTokenize.length > 5) tipVozvrata = lineTokenize[5];
                    if (lineTokenize.length > 8) sostoyanie = lineTokenize[8];

                    if (true == tipVozvrata.startsWith("принял Сервер")) {
                       tipVozvrata = new String("на Широнинцев");
                        if (true != sostoyanie.startsWith("Зачет на баланс")) {
                            tipVozvrata = new String("НА ШИРОНИНЦЕВ");
                        }
                    } else if (true == tipVozvrata.startsWith("Отправили на ДС-Линк")) {
                                tipVozvrata = new String("Отправлено нам");
                                if (true != sostoyanie.startsWith("Зачет на баланс")) {
                                    tipVozvrata = new String("ОТПРАВЛЕНО НАМ");
                                }
                            };
                    if (true != sostoyanie.startsWith("Зачет на баланс")){
                            tipVozvrata = new String(tipVozvrata.toUpperCase());
                    }
                    
                    rightNow.setTime(currentDate);
                    rightNow.add(Calendar.DAY_OF_MONTH, 0);
                    Date weekAgo = rightNow.getTime();

                    if (crDate.before(weekAgo))
                        shippingData.add( new Shipping( bID, crDate, marka, supl, tipVozvrata) );
                }
                return true;
            } else {
                System.out.println("Foreign data");
                JOptionPane.showMessageDialog(msgFrame, "Data in clipboard buffer not match!");
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(msgFrame, ex);
            return false;
        }
    }
}
 /*
Brak_ID	дата созданияr	Тип	supl	марка	tip_vozvr	d_otpr	SS	Состояние брака
70769	21.10.2014	M/board	Рома	Biostar A58ML Socket FM2+	Готов к отправке              		44,00	Еще не тестировали
70643	17.10.2014	M/board	Рома	Biostar B75MU3B Socket 1155	Готов к отправке              		50,50	Еще не тестировали
70795	21.10.2014	Планшетные ПК	Рома	Планшетный ПК Ainol Novo 7 Venus 16Gb Black	Готов к отправке              		108,01	Еще не тестировали
70791	21.10.2014	HDD-накопители	ASBIS	HDD SATA 1.0Tb  TOSHIBA, 32Mb (DT01ACA100)	Готов к отправке              		58,64	Еще не тестировали
70837	21.10.2014	HDD-накопители	ASBIS	HDD SATA 2.0Tb WD, 64Mb, Caviar RED ( WD20EFRX)	Готов к отправке              		115,03	Еще не тестировали
70863	22.10.2014	Мониторы	MST	"Philips 21.5"" TFT 226V4LAB/01 Black"	Готов к отправке              		105,00	Еще не тестировали
70879	23.10.2014	HDD-накопители	ELKO	HDD SATA  500Gb Seagate  7200RPM 16Mb (ST500DM002)	Готов к отправке              		48,20	Еще не тестировали
70876	22.10.2014	Net Active	ELKO	Беспроводной маршрутизатор TP-LINK TL-WR841N (1*Wan, 4*Lan, WiFi 802.11n, 2 антенны)	Отправили на ДС-Линк          		20,82	Протестировали
70860	22.10.2014	HDD внешние	Технопарк	"HDD ext 2.5"" USB 1Tb TRANSCEND StoreJet (TS1TSJ25M3)"	Готов к отправке              		78,50	Еще не тестировали
70862	22.10.2014	HDD внешние	Технопарк	"HDD ext 2.5"" USB 1Tb TRANSCEND StoreJet (TS1TSJ25M3)"	Готов к отправке              		78,50	Еще не тестировали
70644	17.10.2014	M/board	Орси	MSI B75MA-E31 Socket 1155	Готов к отправке              		50,50	Еще не тестировали
70635	17.10.2014	02 iPhone (5/5S)	Орси	Ozaki O!coat 0.3 Jelly Transparent for iPhone 5 (OC533TR)	Готов к отправке              		11,59	Еще не тестировали
70831	21.10.2014	Servers	Serol	Intel Xeon E5-2697v2 (2700MHz, 30MB, S2011) tray	Готов к отправке              		2600,00	Еще не тестировали
70870	22.10.2014	Клавиатуры	Gembird UA	Клавиатура Gembird KB-P4-W-UA White wireless	Готов к отправке              		24,00	Еще не тестировали
70871	22.10.2014	Манипуляторы	Gembird UA	Мышь Gembird MUSW-101-B wireless синяя	Готов к отправке              		6,00	Еще не тестировали
70850	22.10.2014	Клавиатуры	ELETEK	Клавиатура ZALMAN ZM-K300M мультимедийная, USB	Готов к отправке              		8,91	Еще не тестировали
70872	22.10.2014	Клавиатуры	NIS	Клавиатура 1808 Slim Black Waterproof PS/2	Готов к отправке              		3,25	Зачет на баланс
70866	22.10.2014	Планшетные ПК	NIS	Планшетний ПК KARBONN 737	Готов к отправке              		39,50	Еще не тестировали
70731	20.10.2014	Кабельная продукция	Полежаев	Кабель Atcom VGA 1,8м HD15M/HD15M с 2-мя фер. Кольцами	Готов к отправке              		1,50	Еще не тестировали
70732	20.10.2014	Звуковые карты	Полежаев	Контроллер USB-sound card (5.1) 3D sound	Готов к отправке              		2,96	Еще не тестировали
70512	14.10.2014	Flash	GOODRAM	SDHC 32Gb Class 10 Goodram	Готов к отправке              		13,50	Зачет на баланс
70851	22.10.2014	Flash	GOODRAM	USB  32Gb Goodram Twister	принял Сервер                 		10,88	Зачет на баланс
70801	21.10.2014	Планшетные ПК	Prexim-D	Планшетный ПК ARCHOS 101 COBALT 8GB ( 502280)	Готов к отправке              		130,00	Еще не тестировали
70654	17.10.2014	RAM	tng	DDR3 4GB/1333 Hynix/3rd	Готов к отправке              		33,01	Еще не тестировали
70819	21.10.2014	RAM	tng	DDR3 4GB/1333 Hynix/3rd	Готов к отправке              		33,01	Еще не тестировали
70857	22.10.2014	Flash	tng	MicroSDXC 64GB Kingston  Class 10 + SD-adapter (SDCX10/64GB)	Готов к отправке              		32,90	Еще не тестировали
70803	21.10.2014	Flash	tng	SDHC 16GB Class 10 Kingston (SD10V/16GB)	Готов к отправке              		7,25	Еще не тестировали
70802	21.10.2014	Flash	tng	USB 8GB Kingston DataTraveler 101 G2 (DT101G2/8GB) Red	Готов к отправке              		3,95	Еще не тестировали
70873	22.10.2014	Flash	tng	USB3.0 64Gb Kingston DataTraveler 111 (DT111/64GB)	Готов к отправке              		25,50	Еще не тестировали
70440	13.10.2014		КПИ-Сервис	IP-Телефон Grand K-333W (2-ports, LCD display, SIP)	Готов к отправке              		15,20	Зачет на баланс
70345	09.10.2014	Манипуляторы	КПИ-Сервис	USB Ewel hand-held Mouse	Готов к отправке              		1,52	Зачет на баланс
70878	22.10.2014	Гаджеты	КПИ-Сервис	USB Ewel вентилятор	Готов к отправке              		0,76	Еще не тестировали
70835	21.10.2014	Корпуса и БП	x2	Блок Питания Banditpower 450W	Готов к отправке              		14,00	Еще не тестировали
70085	03.10.2014	Акустика	СЦ313	SVEN SPS-604 черный	Готов к отправке              		7,55	Еще не тестировали
70868	22.10.2014	Notebook	MTI списания	Asus X502CA (X502CA-XX007D) Dark	Готов к отправке              		340,00	Еще не тестировали
70869	22.10.2014	Notebook	MTI списания	Lenovo ThinkPad E440 (20C5A03200) Black	Готов к отправке              		580,00	Еще не тестировали
70867	22.10.2014	Планшетные ПК	MTI списания	Планшетный ПК Lenovo IdeaPad B8080F (59412202)	Готов к отправке              		330,00	Зачет на баланс
70799	21.10.2014	SSD-накопители	Евроконтакт	SSD  32Gb  Crucial® V4 (CT032V4SSD2)	Готов к отправке              		42,00	Еще не тестировали
70812	21.10.2014	SSD-накопители	Евроконтакт	SSD 128GB OCZ Vertex 450 (SATA III, VTX450-25SAT3-128G)	Готов к отправке              		110,00	Еще не тестировали
70414	11.10.2014	Наушники и  микрофоны	NeoLogic	Гарнитура Flyper Delux FDH500	Готов к отправке              		8,05	Еще не тестировали
70779	21.10.2014	M/board	EPOS	Asus F1A75-V EVO Socket FM1	Готов к отправке              		89,80	Еще не тестировали
70603	16.10.2014	M/board	EPOS	Asus H97-PRO Socket 1150	Готов к отправке              		129,00	Еще не тестировали
70651	17.10.2014	M/board	EPOS	Asus P8H61-M LX2 (REV 3.0) Socket 1155	Готов к отправке              		49,50	Еще не тестировали
70715	20.10.2014	Videocards	EPOS	GF GT630 1Gb DDR3 ASUS (GT630-SL-1GD3-L)	Готов к отправке              		51,36	Зачет на баланс
70668	17.10.2014	Videocards	EPOS	GF GTX550Ti 1Gb GDDR5 ASUS (ENGTX550 Ti/DI/1GD5)	Готов к отправке              		112,16	Еще не тестировали
70175	04.10.2014	Мелкая бытовая техника	Enter_Marina	Тепловентилятор Wild Wind WWP-FH-06-2,0-G	Готов к отправке              		14,01	Еще не тестировали
70337	08.10.2014	Климатическая техника	Enter_Marina	Тепловентилятор Wild Wind WWP-FH-06-2,0-G	Готов к отправке              		14,01	Зачет на баланс
69829	26.09.2014	RAM	Team	DDR1 1GB/400  Team Elite (TED11G400HC301)	Готов к отправке              		17,47	Зачет на баланс
69967	30.09.2014	RAM	Team	DDR1 1GB/400  Team Elite (TEDR1024M400HC3)	Готов к отправке              		18,20	Зачет на баланс
69767	25.09.2014	RAM	Team	DDR1 2x1GB 400MHz  Team Elite Plus (TPD12G400HC3DC01)	Готов к отправке              		35,45	Зачет на баланс
70676	18.10.2014	RAM	Team	DDR1 2x1GB 400MHz  Team Elite Plus (TPD12G400HC3DC01)	Готов к отправке              		35,45	Зачет на баланс
70129	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL5 (TED22G800HC501)	Готов к отправке              		24,90	Зачет на баланс
69525	18.09.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
69916	29.09.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
70130	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
70131	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
70150	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
70242	06.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
70412	11.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	Готов к отправке              		25,24	Зачет на баланс
69826	26.09.2014	RAM	Team	DDR3 1GB/1333 Team Elite (TED31G1333C901)	Готов к отправке              		7,85	Зачет на баланс
69948	29.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32048M1333HC9)	Готов к отправке              			Зачет на баланс
70647	17.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32048M1333HC9)	Готов к отправке              			Еще не тестировали
69380	12.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69680	22.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69681	22.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69831	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69832	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69833	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69834	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69835	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69836	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69837	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69838	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69839	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69840	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69913	29.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70080	02.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70128	03.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70147	03.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70230	06.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70442	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70443	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70445	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70476	14.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70522	14.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70241	06.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32GM1333HC901)	Готов к отправке              		15,19	Зачет на баланс
70243	06.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32GM1333HC901)	Готов к отправке              		15,19	Зачет на баланс
70096	03.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70397	10.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70432	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
70438	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	Готов к отправке              		18,93	Зачет на баланс
69800	25.09.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Зачет на баланс
69801	25.09.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Зачет на баланс
70094	03.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Зачет на баланс
70148	03.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Зачет на баланс
70204	06.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Зачет на баланс
70585	16.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Еще не тестировали
70775	21.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	Готов к отправке              		19,55	Зачет на баланс
70195	06.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite Plus (TPD32G1600HC1101)	Готов к отправке              		19,20	Зачет на баланс
70856	22.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite Plus (TPD32G1600HC1101)	Готов к отправке              		19,20	Еще не тестировали
69202	08.09.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark Blue, (TDBD38G1600HC9DC01)	Готов к отправке              		72,08	Зачет на баланс
70042	02.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD38G1600HC9DC01)	Готов к отправке              		72,11	Зачет на баланс
70134	03.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD38G1600HC9DC01)	Готов к отправке              		72,11	Зачет на баланс
70135	03.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD38G1600HC9DC01)	Готов к отправке              		72,11	Зачет на баланс
70844	22.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Vulcan Orange, 9-9-9-24 (TLAD38G1600HC9DC01)	Готов к отправке              		79,46	Зачет на баланс
69853	27.09.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Vulcan, 9-9-9-24 (TLD38G1600HC9DC01)	Готов к отправке              		74,67	Зачет на баланс
69918	29.09.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Vulcan, 9-9-9-24 (TLD38G1600HC9DC01)	Готов к отправке              		74,67	Зачет на баланс
70132	03.10.2014	RAM	Team	DDR3 2x4GB 1866MHz Team Xtreem Dark, 9-11-9-27 (TDD38G1866HC9KDC01)	Готов к отправке              		82,00	Зачет на баланс
70477	14.10.2014	RAM	Team	DDR3 2x4GB 2133MHz Team Xtreem Vulcan, 11-11-11-31 (TLD38G2133HC11ADC01)	Готов к отправке              		84,23	Зачет на баланс
70192	06.10.2014	RAM	Team	DDR3 2x8GB 2133MHz Team  Xtreem LV, 11-11-11-28 (TXD316G2133HC11DC01)	Готов к отправке              		156,81	Зачет на баланс
70833	21.10.2014	RAM	Team	DDR3 2x8GB 2133MHz Team  Xtreem LV, 11-11-11-28 (TXD316G2133HC11DC01)	Готов к отправке              		156,81	Еще не тестировали
70516	14.10.2014	RAM	Team	DDR3 2x8GB 2400MHz Team Xtreem LW, 10-12-12-31 (TXD316G2400HC10QDC01)	Готов к отправке              		160,35	Зачет на баланс
70484	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333C901)	Готов к отправке              			Зачет на баланс
69620	19.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
69632	20.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
69830	26.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
69841	27.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
69842	27.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
69843	27.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
69915	29.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70125	03.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70126	03.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70225	06.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70369	09.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70422	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70431	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70435	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70437	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70474	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70504	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70537	15.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Зачет на баланс
70672	17.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	Готов к отправке              		37,00	Еще не тестировали
69212	08.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	Готов к отправке              		37,50	Зачет на баланс
69213	08.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	Готов к отправке              		37,50	Зачет на баланс
70494	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	Готов к отправке              		37,50	Зачет на баланс
70697	20.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	Готов к отправке              		37,50	Зачет на баланс
69623	19.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	Готов к отправке              		36,96	Зачет на баланс
69624	19.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	Готов к отправке              		36,96	Зачет на баланс
70538	15.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	Готов к отправке              		36,96	Зачет на баланс
70564	15.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	Готов к отправке              		36,96	Зачет на баланс
69294	10.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69308	10.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69370	12.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69381	12.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69844	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69845	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69846	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69847	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69848	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69849	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
69938	29.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
70081	03.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
70120	03.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
70424	13.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
70555	15.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
70556	15.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Зачет на баланс
70804	21.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	Готов к отправке              		36,50	Еще не тестировали
69314	10.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34GM1600C1101)	Готов к отправке              			Зачет на баланс
70239	06.10.2014	RAM	Team	DDR3 4GB/1866 Team Elite Plus UD-D3 (TPD34G1866HC1301)	Готов к отправке              		39,73	Зачет на баланс
69816	26.09.2014	RAM	Team	DDR3 4x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD316G1600HC9QC01)	Готов к отправке              		163,18	Зачет на баланс
69733	24.09.2014	RAM	Team	DDR3 8GB/1333 Team Elite (TED38G1333HC901)	Готов к отправке              		70,60	Зачет на баланс
70219	06.10.2014	RAM	Team	DDR3 8GB/1333 Team Elite (TED38G1333HC901)	Готов к отправке              		70,60	Зачет на баланс
69825	26.09.2014	RAM	Team	DDR3 8GB/1333 Team Elite (TED38GM1333HC901)	Готов к отправке              		68,00	Зачет на баланс
69852	27.09.2014	RAM	Team	DDR3 8GB/1333 Team Elite Plus (TPD38G1333HC901)	Готов к отправке              		66,91	Зачет на баланс
69715	23.09.2014	RAM	Team	DDR3 8GB/1600 Team Elite (TED38G1600HC1101)	Готов к отправке              		70,57	Зачет на баланс
69903	29.09.2014	RAM	Team	DDR3 8GB/1600 Team Elite (TED38G1600HC1101)	Готов к отправке              		70,57	Зачет на баланс
70149	03.10.2014	RAM	Team	DDR3 8GB/1600 Team Elite (TED38G1600HC1101)	Готов к отправке              		70,57	Зачет на баланс
70861	22.10.2014	RAM	Team	DDR3 8GB/1600 Team Elite Plus (TPD38G1600HC1101)	Готов к отправке              		66,86	Зачет на баланс
69188	08.09.2014	RAM	Team	DDR3 8GB/1866 Team Elite Plus UD-D3 (TPD38G1866HC1301)	Готов к отправке              		70,70	Зачет на баланс
69772	25.09.2014	RAM	Team	DDR3 8GB/1866 Team Elite Plus UD-D3 (TPD38G1866HC1301)	Готов к отправке              		70,70	Зачет на баланс
69421	15.09.2014	Flash	Team	MicroSD  2Gb Team/no adapter	Готов к отправке              		2,35	Зачет на баланс
69657	22.09.2014	Flash	Team	MicroSD  2Gb Team+2 adapters (SD/miniSD)	Готов к отправке              		3,45	Зачет на баланс
69783	25.09.2014	Flash	Team	microSDHC  4GB Team Class 4/no adapter	Готов к отправке              		2,51	Зачет на баланс
69322	10.09.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Зачет на баланс
69712	23.09.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Зачет на баланс
69889	29.09.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Зачет на баланс
70255	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Зачет на баланс
70284	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70285	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70288	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70289	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70311	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70312	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70313	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70314	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70315	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70316	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
70319	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	Готов к отправке              		2,62	Еще не тестировали
69274	10.09.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	Готов к отправке              		3,85	Зачет на баланс
69275	10.09.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	Готов к отправке              		3,85	Зачет на баланс
69388	13.09.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	Готов к отправке              		3,85	Зачет на баланс
70033	02.10.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	Готов к отправке              		3,85	Зачет на баланс
70283	07.10.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	Готов к отправке              		3,85	Еще не тестировали
70318	07.10.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	Готов к отправке              		3,85	Еще не тестировали
69295	10.09.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	Готов к отправке              		3,21	Зачет на баланс
69882	29.09.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	Готов к отправке              		3,21	Зачет на баланс
69883	29.09.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	Готов к отправке              		3,21	Зачет на баланс
70257	07.10.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	Готов к отправке              		3,21	Зачет на баланс
70287	07.10.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	Готов к отправке              		3,21	Еще не тестировали
70294	08.10.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	Готов к отправке              		3,21	Зачет на баланс
69503	17.09.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	Готов к отправке              		6,26	Зачет на баланс
70256	07.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	Готов к отправке              		6,26	Зачет на баланс
70320	08.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	Готов к отправке              		6,26	Зачет на баланс
70521	14.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	Готов к отправке              		6,26	Зачет на баланс
70817	21.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	Готов к отправке              		6,26	Зачет на баланс
69515	17.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
69778	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
69779	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
69781	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
69782	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
69921	29.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
70007	30.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Зачет на баланс
70646	17.10.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	Готов к отправке              		5,99	Еще не тестировали
69277	10.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69400	15.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69618	19.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69629	20.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69714	23.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69797	25.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69856	27.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69885	29.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69899	29.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69987	30.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70006	30.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70078	02.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70207	06.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70268	07.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70290	07.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Еще не тестировали
70317	07.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Еще не тестировали
70400	10.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70544	15.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
70737	20.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	Готов к отправке              		11,93	Зачет на баланс
69476	16.09.2014	Flash	Team	MicroSDHC 32GB Team Class 4+ Reader TR11A1	Готов к отправке              		13,50	Зачет на баланс
70010	01.10.2014	Flash	Team	microSDHC 32GB Team Class 4+adapter	Готов к отправке              		11,97	Зачет на баланс
70451	13.10.2014	Flash	Team	microSDHC 32GB Team Class 4+adapter	Готов к отправке              		11,97	Зачет на баланс
69276	10.09.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Зачет на баланс
69746	24.09.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Зачет на баланс
69766	25.09.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Зачет на баланс
70018	01.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Зачет на баланс
70272	07.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Зачет на баланс
70648	17.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Еще не тестировали
70830	21.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	Готов к отправке              		12,70	Зачет на баланс
69373	12.09.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	Готов к отправке              		12,19	Зачет на баланс
69711	23.09.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	Готов к отправке              		12,19	Зачет на баланс
69884	29.09.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	Готов к отправке              		12,19	Зачет на баланс
70074	02.10.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	Готов к отправке              		12,19	Зачет на баланс
70133	03.10.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	Готов к отправке              		12,19	Зачет на баланс
70598	16.10.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	Готов к отправке              		12,19	Зачет на баланс
69745	24.09.2014	Flash	Team	microSDXC 64Gb UHS-1 Team+adapter	Готов к отправке              		28,35	Зачет на баланс
69315	10.09.2014	Flash	Team	SDHC 32Gb Class 10 Team	Готов к отправке              		12,84	Зачет на баланс
70304	08.10.2014	Flash	Team	SDHC 32Gb Class 10 Team	Готов к отправке              		12,84	Зачет на баланс
70774	21.10.2014	Flash	Team	SDHC 32Gb Class 10 Team	Готов к отправке              		12,84	Зачет на баланс
70842	22.10.2014	Flash	Team	SDHC 32Gb Class 10 Team	Готов к отправке              		12,84	Протестировали
70875	22.10.2014	Flash	Team	SDHC 32Gb UHS-1 Class 10 Team (TSDHC32GUHS01)	Готов к отправке              		23,03	Еще не тестировали
70542	15.10.2014	RAM	Team	SO-DIMM 4Gb DDR3 1333 Team (TED34G1333C9-S01)	Готов к отправке              		36,81	Зачет на баланс
69243	09.09.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	Готов к отправке              		65,00	Зачет на баланс
69796	25.09.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	Готов к отправке              		65,00	Зачет на баланс
70124	03.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	Готов к отправке              		65,00	Зачет на баланс
70874	22.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	Готов к отправке              		65,00	Еще не тестировали
70121	03.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1600 Team (TED38G1600C11-S01)	Готов к отправке              		70,12	Зачет на баланс
70122	03.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1600 Team (TED38G1600C11-S01)	Готов к отправке              		70,12	Зачет на баланс
69477	16.09.2014	Flash	Team	Team micro reader  TR11A1 USB2.0 blue  retail	Готов к отправке              		1,41	Зачет на баланс
69478	16.09.2014	Flash	Team	Team micro reader  TR11A1 USB2.0 blue  retail	Готов к отправке              		1,41	Зачет на баланс
69947	29.09.2014	Flash	Team	USB  32Gb Team TL01 Brown	Готов к отправке              		13,50	Зачет на баланс
70039	02.10.2014	Flash	Team	USB  32Gb Team TL01 Brown	Готов к отправке              		13,50	Зачет на баланс
69172	06.09.2014	Flash	Team	USB  8Gb Team C111 Red	Готов к отправке              		3,55	Зачет на баланс
69194	08.09.2014	Flash	Team	USB  8Gb Team C112 Gray	Готов к отправке              		3,42	Зачет на баланс
70224	06.10.2014	Flash	Team	USB  8Gb Team C117 Iron	Готов к отправке              		5,60	Зачет на баланс
70321	08.10.2014	Flash	Team	USB  8Gb Team Diamond  Iron	Готов к отправке              		4,63	Еще не тестировали
70428	13.10.2014	Flash	Team	USB  8Gb Team SR3 Red	Готов к отправке              		3,39	Зачет на баланс
69480	16.09.2014	Flash	Team	USB 16Gb Team C111 Blue	Готов к отправке              		5,43	Зачет на баланс
69626	19.09.2014	Flash	Team	USB 16Gb Team C112 Blue	Готов к отправке              		5,55	Зачет на баланс
69658	22.09.2014	Flash	Team	USB 16Gb Team C12G Black	Готов к отправке              		6,20	Зачет на баланс
70069	02.10.2014	Flash	Team	USB 16Gb Team Color Turn Green	Готов к отправке              		5,44	Зачет на баланс
70323	08.10.2014	Flash	Team	USB 16Gb Team Diamond  Iron	Готов к отправке              		6,95	Еще не тестировали
70267	07.10.2014	Flash	Team	USB 16Gb Team F108 Brown	Готов к отправке              		5,47	Зачет на баланс
70782	21.10.2014	Flash	Team	USB 16Gb Team F108 Brown	Готов к отправке              		5,47	Зачет на баланс
69321	10.09.2014	Flash	Team	USB 2Gb Team Color Turn Green (TE9022GG01)	Готов к отправке              		3,30	Зачет на баланс
70324	08.10.2014	Flash	Team	USB 4Gb Team Diamond  Gold	Готов к отправке              		4,43	Еще не тестировали
70327	08.10.2014	Flash	Team	USB 4Gb Team Diamond  Gold	Готов к отправке              		4,43	Еще не тестировали
70328	08.10.2014	Flash	Team	USB 4Gb Team Diamond  Gold	Готов к отправке              		4,43	Еще не тестировали
70025	01.10.2014	Flash	Team	USB 8Gb Team C126 Blue	Готов к отправке              		3,39	Зачет на баланс
69945	29.09.2014	Flash	Team	USB 8Gb Team C12G White	Готов к отправке              		4,12	Зачет на баланс
70852	22.10.2014	Flash	Team	USB 8Gb Team C12G White	Готов к отправке              		4,12	Зачет на баланс
70776	21.10.2014	Flash	Team	USB 8Gb Team Color Turn Brown	Готов к отправке              		3,42	Зачет на баланс
70618	16.10.2014	Flash	Team	USB 8Gb Team F108 Xmas	Готов к отправке              		10,39	Зачет на баланс
70298	08.10.2014	Flash	Team	USB3.0 16Gb Team C123 White	Готов к отправке              		7,25	Зачет на баланс
70277	07.10.2014	Flash	Team	USB3.0 32Gb Team C123 Gray	Готов к отправке              		11,00	Зачет на баланс
70855	22.10.2014	Flash	Team	USB3.0 64Gb Team X121 Red (TX12164GR01)	Готов к отправке              		46,15	Еще не тестировали
70024	01.10.2014	Flash	Team	USB3.0 8Gb Team C123 Gray	Готов к отправке              		5,30	Зачет на баланс
70712	20.10.2014	HDD-накопители	Цурка-Bell	"HDD 2.5"" SATA  500Gb Toshiba, 8Mb,  MQ01ABD050"	Готов к отправке              		41,37	Еще не тестировали
70713	20.10.2014	HDD-накопители	Цурка-Bell	"HDD 2.5"" SATA  500Gb Toshiba, 8Mb,  MQ01ABD050"	Готов к отправке              		41,37	Еще не тестировали
70172	04.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70467	13.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Зачет на баланс
70468	13.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Зачет на баланс
70701	20.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70704	20.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70709	20.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70711	20.10.2014	HDD-накопители	Цурка-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70640	17.10.2014	HDD-накопители	Цурка-Bell	HDD SATA 1.0Tb  TOSHIBA, 32Mb (DT01ACA100)	Готов к отправке              		58,64	Еще не тестировали
70552	15.10.2014	HDD-накопители	Цурка-Bell	HDD SATA 2.0Tb  TOSHIBA, 6Gb/s, 64Mb (DT01ACA200)	Готов к отправке              		83,82	Еще не тестировали
70827	21.10.2014	Моб.телефоны	Valsys	Lenovo A369 Black_ из ремонта (863713027990377)	Готов к отправке              		44,00	Еще не тестировали
70826	21.10.2014	Моб.телефоны	Valsys	Lenovo A760 White_ из ремонта (860227024939546)	Готов к отправке              		59,00	Еще не тестировали
70718	20.10.2014	Моб.телефоны	Valsys	Lenovo S650 Vibe X mini Silver_	Готов к отправке              		135,00	Протестировали
70823	21.10.2014	Моб.телефоны	Valsys	Lenovo А516 White_	Готов к отправке              		111,00	Протестировали
70854	22.10.2014	Моб.телефоны	Valsys	Lenovo А820 Black_	Готов к отправке              		139,39	Еще не тестировали
68697	22.08.2014	CPU	Server_Alex	Athlon II X4 750K (Socket FM2) BOX	Готов к отправке              	29.08.2014	86,94	Зачет на баланс
70620	16.10.2014	Мелкая бытовая техника	ЕТК	Утюг SCARLETT SC-SI30K01 (белый с зеленым) DDP	Готов к отправке              		20,46	Еще не тестировали
70797	21.10.2014	Планшетные ПК	Бонус планшеты	Планшетный ПК Archos 101 IT 16GB (501594) Ref. царапины на корпусе	Готов к отправке              		190,00	Еще не тестировали
70514	14.10.2014	HDD-накопители	цурка2	HDD SATA 320Gb Seagate 5900RPM 8Mb (ST3320310CS)	Готов к отправке              		27,05	Зачет на баланс
70762	21.10.2014	Мелкая бытовая техника	Optovik	Мультиварка SHIVAKI SMC 8651	Готов к отправке              		40,00	Зачет на баланс
54378	14.10.2013	ИБП, АВР и батареи	SVEN брак	Hardity UP-1500 черный	Готов к отправке              	23.01.2014	55,53	Зачет на баланс
70730	20.10.2014	Клавиатуры	SVEN брак	SVEN Comfort 7400EL черный USB	Готов к отправке              		17,83	Зачет на баланс
70395	10.10.2014	Акустика	SVEN брак	SVEN MS-1085 UAH	Готов к отправке              		42,22	Протестировали
66959	08.07.2014	Акустика	SVEN брак	SVEN MS-230 персик	Готов к отправке              		17,04	Зачет на баланс
70796	21.10.2014	Манипуляторы	SVEN брак	SVEN RX-165 USB	Готов к отправке              		2,83	Еще не тестировали
70744	20.10.2014	Наушники и  микрофоны	SVEN брак	SVEN SEB 12 WD черный/серебро UAH	Готов к отправке              		5,15	Зачет на баланс
70843	22.10.2014	Наушники и  микрофоны	SVEN брак	SVEN SEB-120 UAH	Готов к отправке              		2,57	Зачет на баланс
66211	17.06.2014	Наушники и  микрофоны	SVEN брак	SVEN SEB-160 Glamour	Готов к отправке              		4,11	Зачет на баланс
70742	20.10.2014	Наушники и  микрофоны	SVEN брак	SVEN SEB-200 черный-красный UAH	Готов к отправке              		4,25	Зачет на баланс
70741	20.10.2014	Наушники и  микрофоны	Анкор	Гарнитура Genius HS-G550 (31710040101)	Готов к отправке              		29,00	Еще не тестировали
70840	22.10.2014	Мелкая бытовая техника	Склад - Чайка	Сушка для фруктов и овощей ZELMER FD1001_	Готов к отправке              		46,50	Зачет на баланс
68994	01.09.2014	CPU	цурка3	Athlon 64 II X2 245 (Socket AM3) tray	Готов к отправке              		28,24	Зачет на баланс
70864	22.10.2014	CPU	цурка3	Athlon 64 II X2 245 (Socket AM3) tray	Готов к отправке              		28,24	Еще не тестировали
67407	19.07.2014	CPU	цурка3	Athlon II X2 340 (Socket FM2) BOX	Готов к отправке              		31,67	Зачет на баланс
69272	10.09.2014	CPU	цурка3	Athlon II X2 340 (Socket FM2) BOX	Готов к отправке              		31,67	Зачет на баланс
69449	16.09.2014	CPU	цурка3	Athlon II X4 740 (Socket FM2) BOX	Готов к отправке              		67,25	Зачет на баланс
70044	02.10.2014	HDD-накопители	цурка3	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70642	17.10.2014	HDD-накопители	цурка3	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
70747	20.10.2014	HDD-накопители	цурка3	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	Готов к отправке              		51,94	Еще не тестировали
68041	06.08.2014	Net Active	цурка4	Беспроводной маршрутизатор MikroTik RB951G-2HnD	Готов к отправке              		62,99	Зачет на баланс
69871	27.09.2014	Net Active	цурка4	Беспроводной маршрутизатор MikroTik RB951G-2HnD	Готов к отправке              		62,99	Еще не тестировали
70757	21.10.2014	Net Active	цурка4	Беспроводной маршрутизатор MikroTik RB951G-2HnD	Готов к отправке              		62,99	Еще не тестировали
70401	10.10.2014	Net Active	цурка4	Беспроводной маршрутизатор MikroTik RB951Ui-2HND	Готов к отправке              		47,61	Еще не тестировали
70280	07.10.2014	Net Active	цурка4	Точка доступа с Антеной Ubiquiti AirMax NanoBridge M5 22 dBi dc9fdb4cec0f	Готов к отправке              		73,70	Еще не тестировали
70773	21.10.2014	Net Active	цурка4	Точка доступа с Антеной Ubiquiti Nanobeam NBE-M2-400 (2Ghz, 18dBi)	Готов к отправке              		69,80	Еще не тестировали
70841	22.10.2014	Pad	CN mouse pad - Sara	Игровая поверхность ProLogix GMP-Speed 250 War Thunder	Готов к отправке              		2,33	Еще не тестировали
70183	06.10.2014	Net Active	цурка5	Точка доступа Ubiquiti Nanostation M2(NS-M2) внешняя/внутренняя, 2GHz, до 7км, 11dBi	Готов к отправке              		73,32	Еще не тестировали
70877	22.10.2014	RAM	СПД Ченцов Д.М.	DDR2 2GB/800 Samsung (M378T5663QZ3-CF7)	Готов к отправке              		23,68	Еще не тестировали
70336	08.10.2014	02 iPhone (5/5S)	Smart Group Чехлы	ITSKINS Fusion Alu Core for iPhone 5/5S Black (APH5-FUSAL-BLCK)	Готов к отправке              		9,00	Еще не тестировали
70858	22.10.2014	14 Lenovo	Smart Group Чехлы	LENOVO Folio Case BC for Lenovo S820 Black (FCBCLS820B)	Готов к отправке              		5,00	Еще не тестировали
70695	20.10.2014	HDD-накопители	Цурка-Софья-Also	"HDD 2.5"" SATA  500Gb Hitachi, 8Mb, Z5K500 (HTS545050A7E380, 0J11285)"	Готов к отправке              		42,92	Еще не тестировали
70557	15.10.2014	HDD-накопители	Цурка-Софья-Also	HDD SATA 1.0Tb Seagate, 64Mb, 7200.14 (ST1000DM003)	Готов к отправке              		54,55	Еще не тестировали
70628	17.10.2014	HDD-накопители	Цурка-Софья-Also	HDD SATA 1.0Tb Seagate, 64Mb, 7200.14 (ST1000DM003)	Готов к отправке              		54,55	Еще не тестировали
70853	22.10.2014	HDD-накопители	Цурка-Софья-Also	HDD SATA 1.0Tb Seagate, 64Mb, 7200.14 (ST1000DM003)	Готов к отправке              		54,55	Еще не тестировали
70859	22.10.2014	HDD-накопители	Цурка-Софья-Also	HDD SATA 2.0Tb Seagate, 64Mb, SV35 (ST2000VX000)	Готов к отправке              		88,40	Еще не тестировали
70703	20.10.2014	Климатическая техника	ROTEX	Тепловентилятор Schtaiger SHG-200-A	Готов к отправке              		7,90	Еще не тестировали
*/