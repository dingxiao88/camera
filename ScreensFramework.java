package CAM;
/*
 * MK3�涯����ϵͳ-�˻�ģ�����������
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 */


import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;



/**
 *
 * @author DX
 */

//@DX1-����Ż���ʶ


public class ScreensFramework extends Application {

	//@1-������stage
    public static Stage MK3_Stage;
    //@2-������scene
    public static Scene MK3_Scene;

	//@3-�������root
    public static Group root = new Group();
	//@4-ҳ������л�login->Main
	public static SimpleStringProperty PageChange = new SimpleStringProperty();
	//@5-��������ʾ��ʱ��
	private static Servo_DisplayTimer Time_Displsy;

	//@7-��ҳ�����б�־
	public static boolean Main_Falg=true;

	//@8-ҳ��λ��ָʾ
	public static int  App_Page=0;    //0:login  1:main  2:performance test  3:help

	//@9-������������
    private ScreensController mainContainer;

    //@10-���泣��
    public static String screen1ID = "main";
    public static String screen1File = "Cam.fxml";
//    public static String screen2ID = "main";
//    public static String screen2File = "Lottery.fxml";
//    public static String screen3ID = "performance_test";
//    public static String screen3File = "Performance_Test.fxml";
//    public static String screen4ID = "checkdata";
//    public static String screen4File = "CheckData.fxml";
//    public static String screen5ID = "set";
//    public static String screen5File = "Set.fxml";
//    public static String screen6ID = "help";
//    public static String screen6File = "Help.fxml";


    //@12-���ݴ洢·������
	public static String MAIN_SaveData_Path = new String("D://");
    //@13-�����ļ��洢·��
	public static File MAIN_FileSave_Path;            //���ݴ洢·��
	//@14-�洢�ļ��ļ������б�
	public static File[] MAIN_SaveFileList;
	//@15-��¼�ļ��洢����
	public static int MAIN_SaveFile_Num;

	//@16-��ʾ��
    private static Notification.Notifier Main_Noti;
    private static Notification Noti_Targe;

	//@17-���ڳߴ�
    private Rectangle2D bounds;

    //@-�Զ�����̼�ֵ
    public static KeyCode Key_Code;
    //@-�Զ�����̲�
    public static int Main_Key_Level=0;
    //@-���̴�������
    public static int Key_Count=0;




    @Override
    public void start(Stage primaryStage) {

    	//@1-��ʼ��Stage
    	MK3_Stage = new Stage(StageStyle.DECORATED);
    	MK3_Stage.setTitle("����ͷ����");

    	//@2-����ҳ�������
        mainContainer = new ScreensController();

        mainContainer.loadScreen(screen1ID, screen1File);
//        mainContainer.loadScreen(screen2ID, screen2File);
//        mainContainer.loadScreen(screen3ID, screen3File);
//        mainContainer.loadScreen(screen4ID, screen4File);
//        mainContainer.loadScreen(screen5ID, screen5File);
//        mainContainer.loadScreen(screen6ID, screen6File);


        //@3-ҳ����Ĺ��ظ��ļ�����
    	PageChange.addListener(new ChangeListener<Object>(){
			@Override
			public void changed(ObservableValue<?> arg0, Object arg1, Object newval) {
				// TODO Auto-generated method stub

				//@-�л���������
				if(newval.toString().equals(new String("main")))
				{
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							if(Main_Falg==false)
							Main_Falg=true;

//							if(MainController.Page1_Run_Flag==true)
//							MainController.curve_run_set(true);

							//@-�л���������
							mainContainer.setScreen(screen1ID);
							App_Page=1;

					        MK3_Stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - 1920 / 2);
					        MK3_Stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - 1080 / 2);
							MK3_Stage.setWidth(1280);
							MK3_Stage.setHeight(720);

						}
					});
				}
				//@-�л������ܼ��
				else if(newval.toString().equals(new String("performance_test")))
				{

				}
				//@-�л�������ҳ��
				else if(newval.toString().equals(new String("help")))
				{

				}
			}});

    	//@3-��ʼ��������ҳ��
        mainContainer.setScreen(screen1ID);
        App_Page=1;

		//@DX2-Log��ʼ
//		ScreensFramework.logger_FY.info("---------------Start----------------");
//		ScreensFramework.logger_XH.info("---------------Start----------------");

        //@5-�������洢��������
        Check_File_Num();


        //@6-scene����ҳ��
        root = new Group();
        root.getChildren().addAll(mainContainer);
        MK3_Scene = new Scene(root);

        //@8-stage����
	    MK3_Stage.setScene(MK3_Scene);
	    bounds = Screen.getPrimary().getBounds();
	    MK3_Stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - 1920 / 2);
	    MK3_Stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - 1080 / 2);


	    //@9-stage��ʾ
	    MK3_Stage.show();

		//@10-����ȫ�ֶ�ʱ��-50ms
	    Time_Displsy = new Servo_DisplayTimer(10);
    }


    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }




    /**��ʾ��Ϣ��ʾ
     *
     * @param Show_Mode
     * @param Show_Info
     */
    public static void Show_Noti(String Show_Mode, String Show_Info)
    {
    	String Noti_Title=null;
    	Image  Noti_Image=null;

    	//@1-Noti���
    	switch(Show_Mode)
    	{
    		case "Info":
	        		Noti_Title = new String("Info");
	        		Noti_Image = new Image(ScreensFramework.class.getResourceAsStream("info.png"));
	        		break;
    		case "Error":
	        		Noti_Title = new String("Error");
	        		Noti_Image = new Image(ScreensFramework.class.getResourceAsStream("error.png"));
	        		break;
    		case "Warning":
	        		Noti_Title = new String("Warning");
	        		Noti_Image = new Image(ScreensFramework.class.getResourceAsStream("warning.png"));
	        		break;
    		case "Success":
	        		Noti_Title = new String("Success");
	        		Noti_Image = new Image(ScreensFramework.class.getResourceAsStream("success.png"));
	        		break;

	        default:break;

    	}

    	//@3-����Noti
    	Noti_Targe =  new Notification(Noti_Title, Show_Info, Noti_Image);

    	//@4-��ʾNoti
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main_Noti = Notification.Notifier.INSTANCE;
				Main_Noti.notify(Noti_Targe);
			}
		});
    }



    /**���洢�����ļ�����
     *
     */
    public static void Check_File_Num()
    {
    	MAIN_FileSave_Path = new File(MAIN_SaveData_Path);
    	if(MAIN_FileSave_Path.isDirectory())
    	{
		   	 //��ȡ�ļ��б�
	    	 MAIN_SaveFileList = MAIN_FileSave_Path.listFiles();
		   	 //��ȡ�ļ�����
	    	 MAIN_SaveFile_Num=MAIN_SaveFileList.length;

	    	 //System.out.println("ok"+MAIN_SaveFile_Num);
    	}
    }


	/**�����˳�
	 *
	 */
	public static void cleanAndQuit() {
		Time_Displsy.task_dis.cancel();

		MK3_Stage.close();
		Platform.exit();
		System.exit(0);
	}
}
