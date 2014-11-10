
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
    private static String markerStr = "Brak_ID\t���� ��������r\t���\tsupl\t�����\ttip_vozvr\td_otpr\tSS\t��������� �����";

//    enum TipVozvrata = {
//                NOT_TESTED_YET ("��� �� �����������"),
//                HAS_BEEN_TESTED ("��������������");
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

                    if (true == tipVozvrata.startsWith("������ ������")) {
                       tipVozvrata = new String("�� ����������");
                        if (true != sostoyanie.startsWith("����� �� ������")) {
                            tipVozvrata = new String("�� ����������");
                        }
                    } else if (true == tipVozvrata.startsWith("��������� �� ��-����")) {
                                tipVozvrata = new String("���������� ���");
                                if (true != sostoyanie.startsWith("����� �� ������")) {
                                    tipVozvrata = new String("���������� ���");
                                }
                            };
                    if (true != sostoyanie.startsWith("����� �� ������")){
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
Brak_ID	���� ��������r	���	supl	�����	tip_vozvr	d_otpr	SS	��������� �����
70769	21.10.2014	M/board	����	Biostar A58ML Socket FM2+	����� � ��������              		44,00	��� �� �����������
70643	17.10.2014	M/board	����	Biostar B75MU3B Socket 1155	����� � ��������              		50,50	��� �� �����������
70795	21.10.2014	���������� ��	����	���������� �� Ainol Novo 7 Venus 16Gb Black	����� � ��������              		108,01	��� �� �����������
70791	21.10.2014	HDD-����������	ASBIS	HDD SATA 1.0Tb  TOSHIBA, 32Mb (DT01ACA100)	����� � ��������              		58,64	��� �� �����������
70837	21.10.2014	HDD-����������	ASBIS	HDD SATA 2.0Tb WD, 64Mb, Caviar RED ( WD20EFRX)	����� � ��������              		115,03	��� �� �����������
70863	22.10.2014	��������	MST	"Philips 21.5"" TFT 226V4LAB/01 Black"	����� � ��������              		105,00	��� �� �����������
70879	23.10.2014	HDD-����������	ELKO	HDD SATA  500Gb Seagate  7200RPM 16Mb (ST500DM002)	����� � ��������              		48,20	��� �� �����������
70876	22.10.2014	Net Active	ELKO	������������ ������������� TP-LINK TL-WR841N (1*Wan, 4*Lan, WiFi 802.11n, 2 �������)	��������� �� ��-����          		20,82	��������������
70860	22.10.2014	HDD �������	���������	"HDD ext 2.5"" USB 1Tb TRANSCEND StoreJet (TS1TSJ25M3)"	����� � ��������              		78,50	��� �� �����������
70862	22.10.2014	HDD �������	���������	"HDD ext 2.5"" USB 1Tb TRANSCEND StoreJet (TS1TSJ25M3)"	����� � ��������              		78,50	��� �� �����������
70644	17.10.2014	M/board	����	MSI B75MA-E31 Socket 1155	����� � ��������              		50,50	��� �� �����������
70635	17.10.2014	02 iPhone (5/5S)	����	Ozaki O!coat 0.3 Jelly Transparent for iPhone 5 (OC533TR)	����� � ��������              		11,59	��� �� �����������
70831	21.10.2014	Servers	Serol	Intel Xeon E5-2697v2 (2700MHz, 30MB, S2011) tray	����� � ��������              		2600,00	��� �� �����������
70870	22.10.2014	����������	Gembird UA	���������� Gembird KB-P4-W-UA White wireless	����� � ��������              		24,00	��� �� �����������
70871	22.10.2014	������������	Gembird UA	���� Gembird MUSW-101-B wireless �����	����� � ��������              		6,00	��� �� �����������
70850	22.10.2014	����������	ELETEK	���������� ZALMAN ZM-K300M ��������������, USB	����� � ��������              		8,91	��� �� �����������
70872	22.10.2014	����������	NIS	���������� 1808 Slim Black Waterproof PS/2	����� � ��������              		3,25	����� �� ������
70866	22.10.2014	���������� ��	NIS	���������� �� KARBONN 737	����� � ��������              		39,50	��� �� �����������
70731	20.10.2014	��������� ���������	��������	������ Atcom VGA 1,8� HD15M/HD15M � 2-�� ���. ��������	����� � ��������              		1,50	��� �� �����������
70732	20.10.2014	�������� �����	��������	���������� USB-sound card (5.1) 3D sound	����� � ��������              		2,96	��� �� �����������
70512	14.10.2014	Flash	GOODRAM	SDHC 32Gb Class 10 Goodram	����� � ��������              		13,50	����� �� ������
70851	22.10.2014	Flash	GOODRAM	USB  32Gb Goodram Twister	������ ������                 		10,88	����� �� ������
70801	21.10.2014	���������� ��	Prexim-D	���������� �� ARCHOS 101 COBALT 8GB ( 502280)	����� � ��������              		130,00	��� �� �����������
70654	17.10.2014	RAM	tng	DDR3 4GB/1333 Hynix/3rd	����� � ��������              		33,01	��� �� �����������
70819	21.10.2014	RAM	tng	DDR3 4GB/1333 Hynix/3rd	����� � ��������              		33,01	��� �� �����������
70857	22.10.2014	Flash	tng	MicroSDXC 64GB Kingston  Class 10 + SD-adapter (SDCX10/64GB)	����� � ��������              		32,90	��� �� �����������
70803	21.10.2014	Flash	tng	SDHC 16GB Class 10 Kingston (SD10V/16GB)	����� � ��������              		7,25	��� �� �����������
70802	21.10.2014	Flash	tng	USB 8GB Kingston DataTraveler 101 G2 (DT101G2/8GB) Red	����� � ��������              		3,95	��� �� �����������
70873	22.10.2014	Flash	tng	USB3.0 64Gb Kingston DataTraveler 111 (DT111/64GB)	����� � ��������              		25,50	��� �� �����������
70440	13.10.2014		���-������	IP-������� Grand K-333W (2-ports, LCD display, SIP)	����� � ��������              		15,20	����� �� ������
70345	09.10.2014	������������	���-������	USB Ewel hand-held Mouse	����� � ��������              		1,52	����� �� ������
70878	22.10.2014	�������	���-������	USB Ewel ����������	����� � ��������              		0,76	��� �� �����������
70835	21.10.2014	������� � ��	x2	���� ������� Banditpower 450W	����� � ��������              		14,00	��� �� �����������
70085	03.10.2014	��������	��313	SVEN SPS-604 ������	����� � ��������              		7,55	��� �� �����������
70868	22.10.2014	Notebook	MTI ��������	Asus X502CA (X502CA-XX007D) Dark	����� � ��������              		340,00	��� �� �����������
70869	22.10.2014	Notebook	MTI ��������	Lenovo ThinkPad E440 (20C5A03200) Black	����� � ��������              		580,00	��� �� �����������
70867	22.10.2014	���������� ��	MTI ��������	���������� �� Lenovo IdeaPad B8080F (59412202)	����� � ��������              		330,00	����� �� ������
70799	21.10.2014	SSD-����������	�����������	SSD  32Gb  Crucial� V4 (CT032V4SSD2)	����� � ��������              		42,00	��� �� �����������
70812	21.10.2014	SSD-����������	�����������	SSD 128GB OCZ Vertex 450 (SATA III, VTX450-25SAT3-128G)	����� � ��������              		110,00	��� �� �����������
70414	11.10.2014	�������� �  ���������	NeoLogic	��������� Flyper Delux FDH500	����� � ��������              		8,05	��� �� �����������
70779	21.10.2014	M/board	EPOS	Asus F1A75-V EVO Socket FM1	����� � ��������              		89,80	��� �� �����������
70603	16.10.2014	M/board	EPOS	Asus H97-PRO Socket 1150	����� � ��������              		129,00	��� �� �����������
70651	17.10.2014	M/board	EPOS	Asus P8H61-M LX2 (REV 3.0) Socket 1155	����� � ��������              		49,50	��� �� �����������
70715	20.10.2014	Videocards	EPOS	GF GT630 1Gb DDR3 ASUS (GT630-SL-1GD3-L)	����� � ��������              		51,36	����� �� ������
70668	17.10.2014	Videocards	EPOS	GF GTX550Ti 1Gb GDDR5 ASUS (ENGTX550 Ti/DI/1GD5)	����� � ��������              		112,16	��� �� �����������
70175	04.10.2014	������ ������� �������	Enter_Marina	��������������� Wild Wind WWP-FH-06-2,0-G	����� � ��������              		14,01	��� �� �����������
70337	08.10.2014	������������� �������	Enter_Marina	��������������� Wild Wind WWP-FH-06-2,0-G	����� � ��������              		14,01	����� �� ������
69829	26.09.2014	RAM	Team	DDR1 1GB/400  Team Elite (TED11G400HC301)	����� � ��������              		17,47	����� �� ������
69967	30.09.2014	RAM	Team	DDR1 1GB/400  Team Elite (TEDR1024M400HC3)	����� � ��������              		18,20	����� �� ������
69767	25.09.2014	RAM	Team	DDR1 2x1GB 400MHz  Team Elite Plus (TPD12G400HC3DC01)	����� � ��������              		35,45	����� �� ������
70676	18.10.2014	RAM	Team	DDR1 2x1GB 400MHz  Team Elite Plus (TPD12G400HC3DC01)	����� � ��������              		35,45	����� �� ������
70129	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL5 (TED22G800HC501)	����� � ��������              		24,90	����� �� ������
69525	18.09.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
69916	29.09.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
70130	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
70131	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
70150	03.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
70242	06.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
70412	11.10.2014	RAM	Team	DDR2 2GB/800 Team Elite CL6 (TED22G800HC601)	����� � ��������              		25,24	����� �� ������
69826	26.09.2014	RAM	Team	DDR3 1GB/1333 Team Elite (TED31G1333C901)	����� � ��������              		7,85	����� �� ������
69948	29.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32048M1333HC9)	����� � ��������              			����� �� ������
70647	17.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32048M1333HC9)	����� � ��������              			��� �� �����������
69380	12.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69680	22.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69681	22.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69831	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69832	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69833	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69834	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69835	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69836	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69837	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69838	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69839	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69840	26.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
69913	29.09.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70080	02.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70128	03.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70147	03.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70230	06.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70442	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70443	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70445	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70476	14.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70522	14.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32G1333HC901)	����� � ��������              		18,93	����� �� ������
70241	06.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32GM1333HC901)	����� � ��������              		15,19	����� �� ������
70243	06.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite (TED32GM1333HC901)	����� � ��������              		15,19	����� �� ������
70096	03.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	����� � ��������              		18,93	����� �� ������
70397	10.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	����� � ��������              		18,93	����� �� ������
70432	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	����� � ��������              		18,93	����� �� ������
70438	13.10.2014	RAM	Team	DDR3 2GB/1333 Team Elite Plus (TPD32G1333HC901)	����� � ��������              		18,93	����� �� ������
69800	25.09.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	����� �� ������
69801	25.09.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	����� �� ������
70094	03.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	����� �� ������
70148	03.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	����� �� ������
70204	06.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	����� �� ������
70585	16.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	��� �� �����������
70775	21.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite (TED32G1600HC1101)	����� � ��������              		19,55	����� �� ������
70195	06.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite Plus (TPD32G1600HC1101)	����� � ��������              		19,20	����� �� ������
70856	22.10.2014	RAM	Team	DDR3 2GB/1600 Team Elite Plus (TPD32G1600HC1101)	����� � ��������              		19,20	��� �� �����������
69202	08.09.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark Blue, (TDBD38G1600HC9DC01)	����� � ��������              		72,08	����� �� ������
70042	02.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD38G1600HC9DC01)	����� � ��������              		72,11	����� �� ������
70134	03.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD38G1600HC9DC01)	����� � ��������              		72,11	����� �� ������
70135	03.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD38G1600HC9DC01)	����� � ��������              		72,11	����� �� ������
70844	22.10.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Vulcan Orange, 9-9-9-24 (TLAD38G1600HC9DC01)	����� � ��������              		79,46	����� �� ������
69853	27.09.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Vulcan, 9-9-9-24 (TLD38G1600HC9DC01)	����� � ��������              		74,67	����� �� ������
69918	29.09.2014	RAM	Team	DDR3 2x4GB 1600MHz Team Xtreem Vulcan, 9-9-9-24 (TLD38G1600HC9DC01)	����� � ��������              		74,67	����� �� ������
70132	03.10.2014	RAM	Team	DDR3 2x4GB 1866MHz Team Xtreem Dark, 9-11-9-27 (TDD38G1866HC9KDC01)	����� � ��������              		82,00	����� �� ������
70477	14.10.2014	RAM	Team	DDR3 2x4GB 2133MHz Team Xtreem Vulcan, 11-11-11-31 (TLD38G2133HC11ADC01)	����� � ��������              		84,23	����� �� ������
70192	06.10.2014	RAM	Team	DDR3 2x8GB 2133MHz Team  Xtreem LV, 11-11-11-28 (TXD316G2133HC11DC01)	����� � ��������              		156,81	����� �� ������
70833	21.10.2014	RAM	Team	DDR3 2x8GB 2133MHz Team  Xtreem LV, 11-11-11-28 (TXD316G2133HC11DC01)	����� � ��������              		156,81	��� �� �����������
70516	14.10.2014	RAM	Team	DDR3 2x8GB 2400MHz Team Xtreem LW, 10-12-12-31 (TXD316G2400HC10QDC01)	����� � ��������              		160,35	����� �� ������
70484	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333C901)	����� � ��������              			����� �� ������
69620	19.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
69632	20.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
69830	26.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
69841	27.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
69842	27.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
69843	27.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
69915	29.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70125	03.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70126	03.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70225	06.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70369	09.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70422	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70431	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70435	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70437	13.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70474	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70504	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70537	15.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	����� �� ������
70672	17.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34G1333HC901)	����� � ��������              		37,00	��� �� �����������
69212	08.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	����� � ��������              		37,50	����� �� ������
69213	08.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	����� � ��������              		37,50	����� �� ������
70494	14.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	����� � ��������              		37,50	����� �� ������
70697	20.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite (TED34GM1333HC901)	����� � ��������              		37,50	����� �� ������
69623	19.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	����� � ��������              		36,96	����� �� ������
69624	19.09.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	����� � ��������              		36,96	����� �� ������
70538	15.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	����� � ��������              		36,96	����� �� ������
70564	15.10.2014	RAM	Team	DDR3 4GB/1333 Team Elite Plus (TPD34G1333HC901)	����� � ��������              		36,96	����� �� ������
69294	10.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69308	10.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69370	12.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69381	12.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69844	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69845	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69846	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69847	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69848	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69849	27.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
69938	29.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
70081	03.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
70120	03.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
70424	13.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
70555	15.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
70556	15.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	����� �� ������
70804	21.10.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34G1600HC1101)	����� � ��������              		36,50	��� �� �����������
69314	10.09.2014	RAM	Team	DDR3 4GB/1600 Team Elite (TED34GM1600C1101)	����� � ��������              			����� �� ������
70239	06.10.2014	RAM	Team	DDR3 4GB/1866 Team Elite Plus UD-D3 (TPD34G1866HC1301)	����� � ��������              		39,73	����� �� ������
69816	26.09.2014	RAM	Team	DDR3 4x4GB 1600MHz Team Xtreem Dark, 9-9-9-24 (TDD316G1600HC9QC01)	����� � ��������              		163,18	����� �� ������
69733	24.09.2014	RAM	Team	DDR3 8GB/1333 Team Elite (TED38G1333HC901)	����� � ��������              		70,60	����� �� ������
70219	06.10.2014	RAM	Team	DDR3 8GB/1333 Team Elite (TED38G1333HC901)	����� � ��������              		70,60	����� �� ������
69825	26.09.2014	RAM	Team	DDR3 8GB/1333 Team Elite (TED38GM1333HC901)	����� � ��������              		68,00	����� �� ������
69852	27.09.2014	RAM	Team	DDR3 8GB/1333 Team Elite Plus (TPD38G1333HC901)	����� � ��������              		66,91	����� �� ������
69715	23.09.2014	RAM	Team	DDR3 8GB/1600 Team Elite (TED38G1600HC1101)	����� � ��������              		70,57	����� �� ������
69903	29.09.2014	RAM	Team	DDR3 8GB/1600 Team Elite (TED38G1600HC1101)	����� � ��������              		70,57	����� �� ������
70149	03.10.2014	RAM	Team	DDR3 8GB/1600 Team Elite (TED38G1600HC1101)	����� � ��������              		70,57	����� �� ������
70861	22.10.2014	RAM	Team	DDR3 8GB/1600 Team Elite Plus (TPD38G1600HC1101)	����� � ��������              		66,86	����� �� ������
69188	08.09.2014	RAM	Team	DDR3 8GB/1866 Team Elite Plus UD-D3 (TPD38G1866HC1301)	����� � ��������              		70,70	����� �� ������
69772	25.09.2014	RAM	Team	DDR3 8GB/1866 Team Elite Plus UD-D3 (TPD38G1866HC1301)	����� � ��������              		70,70	����� �� ������
69421	15.09.2014	Flash	Team	MicroSD  2Gb Team/no adapter	����� � ��������              		2,35	����� �� ������
69657	22.09.2014	Flash	Team	MicroSD  2Gb Team+2 adapters (SD/miniSD)	����� � ��������              		3,45	����� �� ������
69783	25.09.2014	Flash	Team	microSDHC  4GB Team Class 4/no adapter	����� � ��������              		2,51	����� �� ������
69322	10.09.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	����� �� ������
69712	23.09.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	����� �� ������
69889	29.09.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	����� �� ������
70255	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	����� �� ������
70284	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70285	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70288	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70289	07.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70311	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70312	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70313	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70314	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70315	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70316	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
70319	08.10.2014	Flash	Team	microSDHC  4GB Team Class 4+adapter	����� � ��������              		2,62	��� �� �����������
69274	10.09.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	����� � ��������              		3,85	����� �� ������
69275	10.09.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	����� � ��������              		3,85	����� �� ������
69388	13.09.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	����� � ��������              		3,85	����� �� ������
70033	02.10.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	����� � ��������              		3,85	����� �� ������
70283	07.10.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	����� � ��������              		3,85	��� �� �����������
70318	07.10.2014	Flash	Team	microSDHC  8GB Team Class 10+adapter	����� � ��������              		3,85	��� �� �����������
69295	10.09.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	����� � ��������              		3,21	����� �� ������
69882	29.09.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	����� � ��������              		3,21	����� �� ������
69883	29.09.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	����� � ��������              		3,21	����� �� ������
70257	07.10.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	����� � ��������              		3,21	����� �� ������
70287	07.10.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	����� � ��������              		3,21	��� �� �����������
70294	08.10.2014	Flash	Team	microSDHC  8GB Team Class 4+adapter	����� � ��������              		3,21	����� �� ������
69503	17.09.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	����� � ��������              		6,26	����� �� ������
70256	07.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	����� � ��������              		6,26	����� �� ������
70320	08.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	����� � ��������              		6,26	����� �� ������
70521	14.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	����� � ��������              		6,26	����� �� ������
70817	21.10.2014	Flash	Team	microSDHC 16GB Team Class 10+adapter	����� � ��������              		6,26	����� �� ������
69515	17.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
69778	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
69779	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
69781	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
69782	25.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
69921	29.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
70007	30.09.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	����� �� ������
70646	17.10.2014	Flash	Team	microSDHC 16GB Team Class 4+adapter	����� � ��������              		5,99	��� �� �����������
69277	10.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69400	15.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69618	19.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69629	20.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69714	23.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69797	25.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69856	27.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69885	29.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69899	29.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69987	30.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70006	30.09.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70078	02.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70207	06.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70268	07.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70290	07.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	��� �� �����������
70317	07.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	��� �� �����������
70400	10.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70544	15.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
70737	20.10.2014	Flash	Team	microSDHC 32GB Team Class 10+adapter	����� � ��������              		11,93	����� �� ������
69476	16.09.2014	Flash	Team	MicroSDHC 32GB Team Class 4+ Reader TR11A1	����� � ��������              		13,50	����� �� ������
70010	01.10.2014	Flash	Team	microSDHC 32GB Team Class 4+adapter	����� � ��������              		11,97	����� �� ������
70451	13.10.2014	Flash	Team	microSDHC 32GB Team Class 4+adapter	����� � ��������              		11,97	����� �� ������
69276	10.09.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	����� �� ������
69746	24.09.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	����� �� ������
69766	25.09.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	����� �� ������
70018	01.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	����� �� ������
70272	07.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	����� �� ������
70648	17.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	��� �� �����������
70830	21.10.2014	Flash	Team	microSDHC 32GB Team Class 6+adapter	����� � ��������              		12,70	����� �� ������
69373	12.09.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	����� � ��������              		12,19	����� �� ������
69711	23.09.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	����� � ��������              		12,19	����� �� ������
69884	29.09.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	����� � ��������              		12,19	����� �� ������
70074	02.10.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	����� � ��������              		12,19	����� �� ������
70133	03.10.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	����� � ��������              		12,19	����� �� ������
70598	16.10.2014	Flash	Team	microSDHC 32Gb UHS-1 Team+adapter	����� � ��������              		12,19	����� �� ������
69745	24.09.2014	Flash	Team	microSDXC 64Gb UHS-1 Team+adapter	����� � ��������              		28,35	����� �� ������
69315	10.09.2014	Flash	Team	SDHC 32Gb Class 10 Team	����� � ��������              		12,84	����� �� ������
70304	08.10.2014	Flash	Team	SDHC 32Gb Class 10 Team	����� � ��������              		12,84	����� �� ������
70774	21.10.2014	Flash	Team	SDHC 32Gb Class 10 Team	����� � ��������              		12,84	����� �� ������
70842	22.10.2014	Flash	Team	SDHC 32Gb Class 10 Team	����� � ��������              		12,84	��������������
70875	22.10.2014	Flash	Team	SDHC 32Gb UHS-1 Class 10 Team (TSDHC32GUHS01)	����� � ��������              		23,03	��� �� �����������
70542	15.10.2014	RAM	Team	SO-DIMM 4Gb DDR3 1333 Team (TED34G1333C9-S01)	����� � ��������              		36,81	����� �� ������
69243	09.09.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	����� � ��������              		65,00	����� �� ������
69796	25.09.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	����� � ��������              		65,00	����� �� ������
70124	03.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	����� � ��������              		65,00	����� �� ������
70874	22.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1333 Team (TED38G1333C9-S01)	����� � ��������              		65,00	��� �� �����������
70121	03.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1600 Team (TED38G1600C11-S01)	����� � ��������              		70,12	����� �� ������
70122	03.10.2014	RAM	Team	SO-DIMM 8Gb DDR3 1600 Team (TED38G1600C11-S01)	����� � ��������              		70,12	����� �� ������
69477	16.09.2014	Flash	Team	Team micro reader  TR11A1 USB2.0 blue  retail	����� � ��������              		1,41	����� �� ������
69478	16.09.2014	Flash	Team	Team micro reader  TR11A1 USB2.0 blue  retail	����� � ��������              		1,41	����� �� ������
69947	29.09.2014	Flash	Team	USB  32Gb Team TL01 Brown	����� � ��������              		13,50	����� �� ������
70039	02.10.2014	Flash	Team	USB  32Gb Team TL01 Brown	����� � ��������              		13,50	����� �� ������
69172	06.09.2014	Flash	Team	USB  8Gb Team C111 Red	����� � ��������              		3,55	����� �� ������
69194	08.09.2014	Flash	Team	USB  8Gb Team C112 Gray	����� � ��������              		3,42	����� �� ������
70224	06.10.2014	Flash	Team	USB  8Gb Team C117 Iron	����� � ��������              		5,60	����� �� ������
70321	08.10.2014	Flash	Team	USB  8Gb Team Diamond  Iron	����� � ��������              		4,63	��� �� �����������
70428	13.10.2014	Flash	Team	USB  8Gb Team SR3 Red	����� � ��������              		3,39	����� �� ������
69480	16.09.2014	Flash	Team	USB 16Gb Team C111 Blue	����� � ��������              		5,43	����� �� ������
69626	19.09.2014	Flash	Team	USB 16Gb Team C112 Blue	����� � ��������              		5,55	����� �� ������
69658	22.09.2014	Flash	Team	USB 16Gb Team C12G Black	����� � ��������              		6,20	����� �� ������
70069	02.10.2014	Flash	Team	USB 16Gb Team Color Turn Green	����� � ��������              		5,44	����� �� ������
70323	08.10.2014	Flash	Team	USB 16Gb Team Diamond  Iron	����� � ��������              		6,95	��� �� �����������
70267	07.10.2014	Flash	Team	USB 16Gb Team F108 Brown	����� � ��������              		5,47	����� �� ������
70782	21.10.2014	Flash	Team	USB 16Gb Team F108 Brown	����� � ��������              		5,47	����� �� ������
69321	10.09.2014	Flash	Team	USB 2Gb Team Color Turn Green (TE9022GG01)	����� � ��������              		3,30	����� �� ������
70324	08.10.2014	Flash	Team	USB 4Gb Team Diamond  Gold	����� � ��������              		4,43	��� �� �����������
70327	08.10.2014	Flash	Team	USB 4Gb Team Diamond  Gold	����� � ��������              		4,43	��� �� �����������
70328	08.10.2014	Flash	Team	USB 4Gb Team Diamond  Gold	����� � ��������              		4,43	��� �� �����������
70025	01.10.2014	Flash	Team	USB 8Gb Team C126 Blue	����� � ��������              		3,39	����� �� ������
69945	29.09.2014	Flash	Team	USB 8Gb Team C12G White	����� � ��������              		4,12	����� �� ������
70852	22.10.2014	Flash	Team	USB 8Gb Team C12G White	����� � ��������              		4,12	����� �� ������
70776	21.10.2014	Flash	Team	USB 8Gb Team Color Turn Brown	����� � ��������              		3,42	����� �� ������
70618	16.10.2014	Flash	Team	USB 8Gb Team F108 Xmas	����� � ��������              		10,39	����� �� ������
70298	08.10.2014	Flash	Team	USB3.0 16Gb Team C123 White	����� � ��������              		7,25	����� �� ������
70277	07.10.2014	Flash	Team	USB3.0 32Gb Team C123 Gray	����� � ��������              		11,00	����� �� ������
70855	22.10.2014	Flash	Team	USB3.0 64Gb Team X121 Red (TX12164GR01)	����� � ��������              		46,15	��� �� �����������
70024	01.10.2014	Flash	Team	USB3.0 8Gb Team C123 Gray	����� � ��������              		5,30	����� �� ������
70712	20.10.2014	HDD-����������	�����-Bell	"HDD 2.5"" SATA  500Gb Toshiba, 8Mb,  MQ01ABD050"	����� � ��������              		41,37	��� �� �����������
70713	20.10.2014	HDD-����������	�����-Bell	"HDD 2.5"" SATA  500Gb Toshiba, 8Mb,  MQ01ABD050"	����� � ��������              		41,37	��� �� �����������
70172	04.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70467	13.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	����� �� ������
70468	13.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	����� �� ������
70701	20.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70704	20.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70709	20.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70711	20.10.2014	HDD-����������	�����-Bell	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70640	17.10.2014	HDD-����������	�����-Bell	HDD SATA 1.0Tb  TOSHIBA, 32Mb (DT01ACA100)	����� � ��������              		58,64	��� �� �����������
70552	15.10.2014	HDD-����������	�����-Bell	HDD SATA 2.0Tb  TOSHIBA, 6Gb/s, 64Mb (DT01ACA200)	����� � ��������              		83,82	��� �� �����������
70827	21.10.2014	���.��������	Valsys	Lenovo A369 Black_ �� ������� (863713027990377)	����� � ��������              		44,00	��� �� �����������
70826	21.10.2014	���.��������	Valsys	Lenovo A760 White_ �� ������� (860227024939546)	����� � ��������              		59,00	��� �� �����������
70718	20.10.2014	���.��������	Valsys	Lenovo S650 Vibe X mini Silver_	����� � ��������              		135,00	��������������
70823	21.10.2014	���.��������	Valsys	Lenovo �516 White_	����� � ��������              		111,00	��������������
70854	22.10.2014	���.��������	Valsys	Lenovo �820 Black_	����� � ��������              		139,39	��� �� �����������
68697	22.08.2014	CPU	Server_Alex	Athlon II X4 750K (Socket FM2) BOX	����� � ��������              	29.08.2014	86,94	����� �� ������
70620	16.10.2014	������ ������� �������	���	���� SCARLETT SC-SI30K01 (����� � �������) DDP	����� � ��������              		20,46	��� �� �����������
70797	21.10.2014	���������� ��	����� ��������	���������� �� Archos 101 IT 16GB (501594) Ref. �������� �� �������	����� � ��������              		190,00	��� �� �����������
70514	14.10.2014	HDD-����������	�����2	HDD SATA 320Gb Seagate 5900RPM 8Mb (ST3320310CS)	����� � ��������              		27,05	����� �� ������
70762	21.10.2014	������ ������� �������	Optovik	����������� SHIVAKI SMC 8651	����� � ��������              		40,00	����� �� ������
54378	14.10.2013	���, ��� � �������	SVEN ����	Hardity UP-1500 ������	����� � ��������              	23.01.2014	55,53	����� �� ������
70730	20.10.2014	����������	SVEN ����	SVEN Comfort 7400EL ������ USB	����� � ��������              		17,83	����� �� ������
70395	10.10.2014	��������	SVEN ����	SVEN MS-1085 UAH	����� � ��������              		42,22	��������������
66959	08.07.2014	��������	SVEN ����	SVEN MS-230 ������	����� � ��������              		17,04	����� �� ������
70796	21.10.2014	������������	SVEN ����	SVEN RX-165 USB	����� � ��������              		2,83	��� �� �����������
70744	20.10.2014	�������� �  ���������	SVEN ����	SVEN SEB 12 WD ������/������� UAH	����� � ��������              		5,15	����� �� ������
70843	22.10.2014	�������� �  ���������	SVEN ����	SVEN SEB-120 UAH	����� � ��������              		2,57	����� �� ������
66211	17.06.2014	�������� �  ���������	SVEN ����	SVEN SEB-160 Glamour	����� � ��������              		4,11	����� �� ������
70742	20.10.2014	�������� �  ���������	SVEN ����	SVEN SEB-200 ������-������� UAH	����� � ��������              		4,25	����� �� ������
70741	20.10.2014	�������� �  ���������	�����	��������� Genius HS-G550 (31710040101)	����� � ��������              		29,00	��� �� �����������
70840	22.10.2014	������ ������� �������	����� - �����	����� ��� ������� � ������ ZELMER FD1001_	����� � ��������              		46,50	����� �� ������
68994	01.09.2014	CPU	�����3	Athlon 64 II X2 245 (Socket AM3) tray	����� � ��������              		28,24	����� �� ������
70864	22.10.2014	CPU	�����3	Athlon 64 II X2 245 (Socket AM3) tray	����� � ��������              		28,24	��� �� �����������
67407	19.07.2014	CPU	�����3	Athlon II X2 340 (Socket FM2) BOX	����� � ��������              		31,67	����� �� ������
69272	10.09.2014	CPU	�����3	Athlon II X2 340 (Socket FM2) BOX	����� � ��������              		31,67	����� �� ������
69449	16.09.2014	CPU	�����3	Athlon II X4 740 (Socket FM2) BOX	����� � ��������              		67,25	����� �� ������
70044	02.10.2014	HDD-����������	�����3	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70642	17.10.2014	HDD-����������	�����3	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
70747	20.10.2014	HDD-����������	�����3	HDD SATA  500Gb TOSHIBA, 32Mb, DT01ACA050	����� � ��������              		51,94	��� �� �����������
68041	06.08.2014	Net Active	�����4	������������ ������������� MikroTik RB951G-2HnD	����� � ��������              		62,99	����� �� ������
69871	27.09.2014	Net Active	�����4	������������ ������������� MikroTik RB951G-2HnD	����� � ��������              		62,99	��� �� �����������
70757	21.10.2014	Net Active	�����4	������������ ������������� MikroTik RB951G-2HnD	����� � ��������              		62,99	��� �� �����������
70401	10.10.2014	Net Active	�����4	������������ ������������� MikroTik RB951Ui-2HND	����� � ��������              		47,61	��� �� �����������
70280	07.10.2014	Net Active	�����4	����� ������� � ������� Ubiquiti AirMax NanoBridge M5 22 dBi dc9fdb4cec0f	����� � ��������              		73,70	��� �� �����������
70773	21.10.2014	Net Active	�����4	����� ������� � ������� Ubiquiti Nanobeam NBE-M2-400 (2Ghz, 18dBi)	����� � ��������              		69,80	��� �� �����������
70841	22.10.2014	Pad	CN mouse pad - Sara	������� ����������� ProLogix GMP-Speed 250 War Thunder	����� � ��������              		2,33	��� �� �����������
70183	06.10.2014	Net Active	�����5	����� ������� Ubiquiti Nanostation M2(NS-M2) �������/����������, 2GHz, �� 7��, 11dBi	����� � ��������              		73,32	��� �� �����������
70877	22.10.2014	RAM	��� ������ �.�.	DDR2 2GB/800 Samsung (M378T5663QZ3-CF7)	����� � ��������              		23,68	��� �� �����������
70336	08.10.2014	02 iPhone (5/5S)	Smart Group �����	ITSKINS Fusion Alu Core for iPhone 5/5S Black (APH5-FUSAL-BLCK)	����� � ��������              		9,00	��� �� �����������
70858	22.10.2014	14 Lenovo	Smart Group �����	LENOVO Folio Case BC for Lenovo S820 Black (FCBCLS820B)	����� � ��������              		5,00	��� �� �����������
70695	20.10.2014	HDD-����������	�����-�����-Also	"HDD 2.5"" SATA  500Gb Hitachi, 8Mb, Z5K500 (HTS545050A7E380, 0J11285)"	����� � ��������              		42,92	��� �� �����������
70557	15.10.2014	HDD-����������	�����-�����-Also	HDD SATA 1.0Tb Seagate, 64Mb, 7200.14 (ST1000DM003)	����� � ��������              		54,55	��� �� �����������
70628	17.10.2014	HDD-����������	�����-�����-Also	HDD SATA 1.0Tb Seagate, 64Mb, 7200.14 (ST1000DM003)	����� � ��������              		54,55	��� �� �����������
70853	22.10.2014	HDD-����������	�����-�����-Also	HDD SATA 1.0Tb Seagate, 64Mb, 7200.14 (ST1000DM003)	����� � ��������              		54,55	��� �� �����������
70859	22.10.2014	HDD-����������	�����-�����-Also	HDD SATA 2.0Tb Seagate, 64Mb, SV35 (ST2000VX000)	����� � ��������              		88,40	��� �� �����������
70703	20.10.2014	������������� �������	ROTEX	��������������� Schtaiger SHG-200-A	����� � ��������              		7,90	��� �� �����������
*/