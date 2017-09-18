package CAM;
/*
 * Copyright (c) 2008, 2013 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.sun.javafx.perf.PerformanceTracker;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import javafx.scene.paint.Color;

/**
 * Login Controller.
 */
public class MainController implements Initializable, ControlledScreen {

	@FXML
	private Button Lottery_Start_Button;

	@FXML
	private ImageView CAM_pic;


//-------------------------------------------------------------------------------------------

	//@1-������Ӧ�ó���ӿ�
	private ScreensController myController;


    //@24-��������ʾͬ��
	public static SimpleStringProperty DisplayProperty_Main = new SimpleStringProperty();
    //@34-��������ʾ������
	private ChangeListener ChangeListen_Display;

	//@6-��ʾ���ݸ�ʽ
	private java.text.NumberFormat  formater_decimal  =  java.text.DecimalFormat.getInstance();  //��ʾС����ʽ��
	private java.text.NumberFormat  AF_formater_value  =  java.text.DecimalFormat.getInstance();  //��ʾС����ʽ��

//	private Image Status_Square_Red;
//	private Image Status_Square_Yellow;
//	private Image Status_Square_Green;
//	private Image Status_Square_White;

	public Image CAM_Image;


	private File file; // �����ļ�����
    //@-����excel������
	private Workbook wb; // ���ļ����л�ȡExcel����������WorkBook��
    //@-��õ�һ�Ź�����
	private Sheet sheet; // �ӹ�������ȡ��ҳ��Sheet��
	public static boolean Lottery_Start_Flag = false;
    private String excel_fileName = "D:\\Name.xls"; // Excel�ļ�����·��
    public ArrayList al=new ArrayList();

    public File file_pic;
    public String localUrl;


	/**�������ʼ��
	 *
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    	//@-���ݾ��ȸ�ʽ - С��
    	formater_decimal.setMaximumFractionDigits(3);
    	formater_decimal.setMinimumFractionDigits(3);

    	//@-��ȡexcel�ļ�
    	//Read_Excel_File();

//    	Status_Square_Red = new Image(MainController.class.getResourceAsStream("statusbar_message_light_red.png"));
//    	Status_Square_Yellow = new Image(MainController.class.getResourceAsStream("statusbar_message_light_orange.png"));
//    	Status_Square_Green = new Image(MainController.class.getResourceAsStream("statusbar_message_light_green.png"));
//    	Status_Square_White = new Image(MainController.class.getResourceAsStream("statusbar_message_light_white.png"));


    	file_pic = new File("/home/pi/Rec/pic/pic.png");

		try {
			localUrl = file_pic.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // don"t load in the background
		//CAM_Image = new Image(localUrl);
    	//CAM_Image = new Image("/home/pi/Rec/pic/pic.png");


    	//@6-��ʾͬ��
    	DisplayProperty_Main.addListener(ChangeListen_Display = new ChangeListener(){
			@Override
			public void changed(ObservableValue arg0, Object oldval, Object newval) {

				Thread t1 = new Thread(new Runnable() {
					@Override
					public void run() {
					// TODO Auto-generated method stub
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							 //################################ 1-���涥����Ϣ��ʾ ##########################################
							if(Lottery_Start_Flag == true)
							{

//						    	file_pic = new File("/home/pi/Rec/pic/pic.png");
//
//								try {
//									localUrl = file_pic.toURI().toURL().toString();
//								} catch (MalformedURLException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}

								//System.out.println(""+localUrl);

								CAM_Image = new Image(localUrl,true);
								CAM_pic.setImage(CAM_Image);
							}
						}
					});
				  }
				});
				t1.setName("MainDisplayUpdate");
				t1.setDaemon(true);
				t1.start();
			}
    	});
    }

    /**
    *
    */
   private void Read_Excel_File()
   {
	   try {
       	   //@-��ȡexcel�ļ�
           file = new File(excel_fileName); // �����ļ�����
           //@-����excel������
           wb = Workbook.getWorkbook(file); // ���ļ����л�ȡExcel����������WorkBook��
           //@-��õ�һ�Ź�����
           sheet = wb.getSheet(0); // �ӹ�������ȡ��ҳ��Sheet��

           //System.out.println(""+sheet.getRows());

           for (int line = 0; line < sheet.getRows(); line++) { // ѭ����ӡExcel���е�����

               for (int row = 0; row < sheet.getColumns(); row++) {
                   Cell cell = sheet.getCell(row, line);

                   al.add(row,new String(cell.getContents()));

                   //System.out.println(al.get(row).toString());
               }
               //System.out.println();
           }
           //System.out.println(""+al.get(23).toString());

       } catch (BiffException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }



    /**
    *
    * @param event
    */
   @FXML
   public void Button_Pro(ActionEvent event)
   {
	   	//@1-��ʼ
	   	if(event.getSource( )== Lottery_Start_Button)
	   	{
	   		//System.out.println("test!\n");
	   		if(Lottery_Start_Flag == false)
	   		{
	   			Lottery_Start_Flag = true;
	   			Lottery_Start_Button.setText("ֹͣ");
	   			Lottery_Start_Button.setId("B3");
	   		}
	   		else if(Lottery_Start_Flag == true)
	   		{
	   			Lottery_Start_Flag = false;
	   			Lottery_Start_Button.setText("��ʼ");
	   			Lottery_Start_Button.setId("B1");
	   		}
	   	}
   }


    /**
     *
     */
	@Override
	public void setScreenParent(ScreensController screenPage) {
		// TODO Auto-generated method stub
		myController = screenPage;
	}

}
