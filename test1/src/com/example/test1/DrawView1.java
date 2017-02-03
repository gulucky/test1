package com.example.test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DrawView1 extends View{
	
	private int width;
	
	private float centerPolygonX;
	private float centerPolygonY;
	
	private float centerCircleX;
	private float centerCircleY;
	
	//����Ǯ��
	private float walletPercent;
	private String walletAll = "";
	private String walletFrozen  = "";
	private String walletRemain = "";
	
	//�ײ�
	private float planPercent;
	private String planAll = "";
	private String planUsed = "";
	private String planRemain = "";
	
	/**
	 * ��Ӧ��ͬ�ֱ���
	 */
	private float ratio;

	public DrawView1(Context context) {
		super(context);
		init(context);
	}
	
	public DrawView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public void setWalletFlow(String walletAll, String walletRemain, String walletFrozen, float walletPercent){
		this.walletAll = walletAll;
		this.walletRemain = walletRemain;
		this.walletFrozen = walletFrozen;
		this.walletPercent = walletPercent;
	}
	
	public void setPlanFlow(String planAll, String planUsed, String planRemain, float planPercent){
		this.planAll = planAll;
		this.planUsed = planUsed;
		this.planRemain = planRemain;
		this.planPercent = planPercent;
	}
	
	
	
	private void init(Context context){
		WindowManager manager = (WindowManager) context 
	            .getSystemService(Context.WINDOW_SERVICE); 
	    Display display = manager.getDefaultDisplay(); 
	    width = display.getWidth();
	    
	    ratio = (float)width / 480;  
	    
	    centerCircleX = width * 7 / 10;
		centerCircleY = (float) (2 * width / 5 + DensityUtil.dip2px(context, 50));
		
		centerPolygonX = width * 3 / 10;
		centerPolygonY = (float) (Math.sqrt(3) / 2 * width / 5 + DensityUtil.dip2px(context, 50));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * ���� ˵�� drawRect ���ƾ��� drawCircle ����Բ�� drawOval ������Բ drawPath ������������
		 * drawLine ����ֱ�� drawPoin ���Ƶ�
		 */
		// ��������
		
		drawCircle(planPercent , canvas);
		drawPolygon(walletPercent , canvas); 
		super.onDraw(canvas);
	}
	
	
	/**
	 * �軭�ٷֱ�ռ�ݵĲ���
	 * @param percent
	 * @param canvas
	 */
	private void drawPerCentPolygon(float percent, Canvas canvas) {
		Paint p = new Paint();
		float radius = width / 5;
		float heightF = (float) (Math.sqrt(3) / 2 * radius);
		p.setColor(Color.RED);
		Path path1 = new Path();
		path1.moveTo(centerPolygonX, centerPolygonY);
		path1.lineTo(centerPolygonX, centerPolygonY - heightF);
		lineToLine(path1, percent, radius);
		GeoPoint geoPoint = calcCoordinate(percent, radius);
		path1.lineTo(geoPoint.x, geoPoint.y);
		path1.close();// ���
		canvas.drawPath(path1, p);

	}
	
	/**
	 * ���Ӱٷֱ�ռ�ݵĶ���
	 * @param path1
	 * @param percent
	 * @param radius
	 */
	private void lineToLine(Path path1, float percent, float radius){
		float heightF = (float) (Math.sqrt(3) / 2 * radius);
		if (percent >= 0 && percent <= 1f / 12) {
			
		}else if (percent <= 3f / 12) {
			path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
		}else if (percent <= 5f / 12) {
			path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
			path1.lineTo(centerPolygonX + radius, centerPolygonY);
		}else if (percent <= 7f / 12) {
			path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
			path1.lineTo(centerPolygonX + radius, centerPolygonY);
			path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
		}else if (percent <= 9f / 12) {
			path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
			path1.lineTo(centerPolygonX + radius, centerPolygonY);
			path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
			path1.lineTo(centerPolygonX - radius / 2, centerPolygonY + heightF );
		}else if (percent <= 11f / 12) {
			path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
			path1.lineTo(centerPolygonX + radius, centerPolygonY);
			path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
			path1.lineTo(centerPolygonX - radius / 2, centerPolygonY + heightF );
			path1.lineTo(centerPolygonX - radius, centerPolygonY);
		}else if (percent <= 1) {
			path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
			path1.lineTo(centerPolygonX + radius, centerPolygonY);
			path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
			path1.lineTo(centerPolygonX - radius / 2, centerPolygonY + heightF );
			path1.lineTo(centerPolygonX - radius, centerPolygonY);
			path1.lineTo(centerPolygonX - radius / 2, centerPolygonY - heightF);
		}
	}
	
	/**
	 * ����
	 * @author Administrator
	 */
	private class GeoPoint{
		public float x;
		public float y;
	}
	
	/**
	 * ��ѧtan����
	 * @param degree
	 * @return
	 */
	private float calcTan(float degree){
		return (float) Math.tan(degree * Math.PI / 180);
	}
	
	/**
	 * ���ٷֱȼ������Ľ�������
	 * @param percent
	 * @param radius
	 * @return
	 */
	private GeoPoint calcCoordinate(float percent, float radius){
		float sq = (float) Math.sqrt(3);
		float heightF = (float) (sq / 2 * radius);
		GeoPoint geoPoint = new GeoPoint();
		if (percent >= 0 && percent <= 1f / 12) {
			float x = heightF * calcTan(360 * percent);
			geoPoint.x = centerPolygonX + x;
			geoPoint.y = centerPolygonY - heightF;
		}else if (percent <= 3f / 12) {
			float x = (radius * calcTan(60)) / (calcTan(90 - 360 * percent) + calcTan(60));
			float y = x * calcTan(90 - 360 * percent);
			geoPoint.x = x + centerPolygonX;
			geoPoint.y = centerPolygonY - y;
		}else if (percent <= 5f / 12) {
			float x = (radius * calcTan(60)) / (calcTan(360 * percent - 90) + calcTan(60));
			float y = x * calcTan(360 * percent - 90);
			geoPoint.x = x + centerPolygonX;
			geoPoint.y = centerPolygonY + y;
		}else if (percent <= 7f / 12) {
			float y = centerPolygonY + heightF;
			float x = heightF * calcTan(Math.abs(360 * percent - 180));
			if (percent <= 0.5f) {
				geoPoint.x = centerPolygonX + x;
			}else {
				geoPoint.x = centerPolygonX - x;
			}
			geoPoint.y = y;
		}else if (percent <= 9f / 12) {
			float x = (radius * calcTan(60)) / (calcTan(270 - 360 * percent) + calcTan(60));
			float y = x * calcTan(270 - 360 * percent);
			geoPoint.x = centerPolygonX - x;
			geoPoint.y = centerPolygonY + y;
		}else if (percent <= 11f / 12) {
			float x = (radius * calcTan(60)) / (calcTan(360 * percent - 270) + calcTan(60));
			float y = x * calcTan(360 * percent - 270);
			geoPoint.x = centerPolygonX - x;
			geoPoint.y = centerPolygonY - y;
		}else if (percent <= 1) {
			float x = heightF * calcTan(Math.abs(360 * percent - 360));
			geoPoint.x = centerPolygonX - x;
			geoPoint.y = centerPolygonY - heightF;
		}
		return geoPoint;
	}

	/**
	 * �軭������
	 * @param percent
	 * @param canvas
	 */
	private void drawPolygon(float percent, Canvas canvas) {
		Paint p = new Paint();
		p.setAntiAlias(true);// ���û��ʵľ��Ч���� true��ȥ�������һ��Ч����������
		//�軭��ײ�Ļ�ɫ������
		float radius = width / 5;
		float heightF = (float) (Math.sqrt(3) / 2 * radius);
		p.setColor(Color.YELLOW);
		Path path1=new Path();
		path1.moveTo(centerPolygonX - radius, centerPolygonY);
		path1.lineTo(centerPolygonX - radius / 2, centerPolygonY - heightF);
		path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
		path1.lineTo(centerPolygonX + radius, centerPolygonY);
		path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
		path1.lineTo(centerPolygonX - radius / 2, centerPolygonY + heightF );
		path1.close();//���
		canvas.drawPath(path1, p);
		
		//���ٷֱ��軭�����κ�ɫ�Ĳ���
		drawPerCentPolygon(percent, canvas);
		
		//�軭��һ��İ�ɫ����������
		radius = width / 5 * 3 / 4 ;
		heightF = (float) (Math.sqrt(3) / 2 * radius);
		p.setColor(Color.WHITE);
		path1=new Path();
		path1.moveTo(centerPolygonX - radius, centerPolygonY);
		path1.lineTo(centerPolygonX - radius / 2, centerPolygonY - heightF);
		path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
		path1.lineTo(centerPolygonX + radius, centerPolygonY);
		path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
		path1.lineTo(centerPolygonX - radius / 2, centerPolygonY + heightF );
		path1.close();//���
		canvas.drawPath(path1, p);
		
		//�軭���Ļ�ɫ������
		radius = width / 5 * 2 / 4 ;
		heightF = (float) (Math.sqrt(3) / 2 * radius);
		p.setColor(Color.GRAY);
		path1=new Path();
		path1.moveTo(centerPolygonX - radius, centerPolygonY);
		path1.lineTo(centerPolygonX - radius / 2, centerPolygonY - heightF);
		path1.lineTo(centerPolygonX + radius / 2, centerPolygonY - heightF);
		path1.lineTo(centerPolygonX + radius, centerPolygonY);
		path1.lineTo(centerPolygonX + radius / 2 , centerPolygonY + heightF);
		path1.lineTo(centerPolygonX - radius / 2, centerPolygonY + heightF );
		path1.close();//���
		canvas.drawPath(path1, p);
		
		//��������
		p.setColor(Color.BLUE);
		p.setTextSize(ratio * 40);
		p.setTextAlign(Align.CENTER);
		canvas.drawText((int)(percent * 100) + "%", centerPolygonX, centerPolygonY - 5, p);// ���ı�
		p.setTextSize(ratio * 17);
		canvas.drawText("����Ǯ��", centerPolygonX, centerPolygonY + ratio * 18, p);// ���ı�
		canvas.drawText("������", centerPolygonX, centerPolygonY + ratio * 18 * 2, p);// ���ı�
		
		//���ò�������
		p.setTextSize(ratio * 18);
		p.setColor(Color.BLACK);
		p.setTextAlign(Align.LEFT);
		radius = width / 5 * 3 / 4 ;
		canvas.drawText("����Ǯ����", centerPolygonX - radius, centerCircleY, p);// ���ı�
		canvas.drawText("������", centerPolygonX - radius, centerCircleY + ratio * 25, p);// ���ı�
		canvas.drawText("��������", centerPolygonX - radius, centerCircleY + ratio * 25 * 2, p);// ���ı�
		canvas.drawText("��������", centerPolygonX - radius, centerCircleY + ratio * 25 * 3, p);// ���ı�
		
		//��������
		canvas.drawLine(centerPolygonX + radius + ratio * 10, centerCircleY - ratio * 15, centerPolygonX + radius + ratio * 10 + 1, centerCircleY + ratio * 25 * 3, p);
		
		
		//��������
		p.setColor(Color.RED);
		p.setTextAlign(Align.RIGHT);
		canvas.drawText(walletAll, centerPolygonX + radius, centerCircleY + ratio * 25, p);// ���ı�
		canvas.drawText(walletRemain, centerPolygonX + radius, centerCircleY + ratio * 25 * 2, p);// ���ı�
		canvas.drawText(walletFrozen, centerPolygonX + radius, centerCircleY + ratio * 25 * 3, p);// ���ı�
		
	}

	/**
	 * �軭Բ��
	 * @param percent
	 * @param canvas
	 */
	private void drawCircle(float percent, Canvas canvas) {
		float degree = percent * 360;
		
		float radius = width / 5;
		
		Paint p = new Paint();
		p.setAntiAlias(true);// ���û��ʵľ��Ч���� true��ȥ�������һ��Ч����������

		//���������Ĵ�Բ
		p.setColor(Color.BLUE);
		canvas.drawCircle(centerCircleX, centerCircleY, radius, p);// ��Բ
		
		//������վ�ٷֱȵĺ�ɫ��
		p.setColor(Color.RED);
		RectF oval2 = new RectF(centerCircleX - radius, centerCircleY - radius, centerCircleX + radius, centerCircleY + radius);// ���ø��µĳ����Σ�ɨ�����
		canvas.drawArc(oval2, -90, degree, true, p);
		
		p.setColor(Color.WHITE);// ���ð�ɫ
		canvas.drawCircle(centerCircleX, centerCircleY, radius * 3 / 4, p);// СԲ
		
		p.setColor(Color.GRAY);// ���û�ɫ
		canvas.drawCircle(centerCircleX, centerCircleY, radius * 2 / 4, p);// СԲ
		
		//��������
		p.setColor(Color.BLUE);
		p.setTextSize(ratio * 40);
		p.setTextAlign(Align.CENTER);
		canvas.drawText((int)(percent * 100) + "%", centerCircleX, centerCircleY - 5, p);// ���ı�
		p.setTextSize(ratio * 17);
		canvas.drawText("�ֻ�����", centerCircleX, centerCircleY + ratio * 18, p);// ���ı�
		canvas.drawText("����", centerCircleX, centerCircleY + ratio * 18 * 2, p);// ���ı�
		
		
		//���ò�������
		radius = width / 5;
		float heightF = (float) (Math.sqrt(3) / 2 * radius);
		p.setTextSize(ratio * 18);
		p.setColor(Color.BLACK);
		p.setTextAlign(Align.LEFT);
		radius = width / 5 * 3 / 4 ;
		float heightText = centerPolygonY - heightF + ratio * 15;
		canvas.drawText("�ֻ��ײ���", centerCircleX - radius + ratio * 10, heightText, p);// ���ı�
		canvas.drawText("������", centerCircleX - radius + ratio * 10, heightText + ratio * 25, p);// ���ı�
		canvas.drawText("��������", centerCircleX - radius + ratio * 10, heightText + ratio * 25 * 2, p);// ���ı�
		canvas.drawText("��������", centerCircleX - radius + ratio * 10, heightText + ratio * 25 * 3, p);// ���ı�
		
		//��������
		canvas.drawLine(centerCircleX - radius, heightText - ratio * 15, centerCircleX - radius + 1, heightText + ratio * 25 * 3, p);
		
		
		//��������
		p.setColor(Color.BLUE);
		p.setTextAlign(Align.RIGHT);
		canvas.drawText(planAll, centerCircleX + radius + ratio * 15, heightText + ratio * 25, p);// ���ı�
		canvas.drawText(planRemain, centerCircleX + radius + ratio * 15, heightText + ratio * 25 * 2, p);// ���ı�
		canvas.drawText(planUsed, centerCircleX + radius + ratio * 15, heightText + ratio * 25 * 3, p);// ���ı�
	}

}
