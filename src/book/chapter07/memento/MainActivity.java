package book.chapter07.memento;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;



public class MainActivity extends Activity {

	private Button chooseDate,add,query;
	private EditText date,subject,body;
	private ListView result;
	private LinearLayout title;
	MyDatabaseHelper mydbHelper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		date=(EditText)this.findViewById(R.id.date);
		subject=(EditText)this.findViewById(R.id.subject);
		body=(EditText)this.findViewById(R.id.body);
		result=(ListView)this.findViewById(R.id.result);
		title=(LinearLayout)this.findViewById(R.id.title);
		add=(Button)this.findViewById(R.id.add);
		query=(Button)this.findViewById(R.id.query);
		chooseDate=(Button)this.findViewById(R.id.chooseDate);
		title.setVisibility(View.INVISIBLE);
		chooseDate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c=Calendar.getInstance();//��ȡ��ǰ����
				
				//����ѡ�����Ի���
				//���ڸı������
				new DatePickerDialog(MainActivity.this,new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int month,int day) {
						// TODO Auto-generated method stub
						//�����ı��༭�������Ϊ���õ����ڣ�month��Ҫ��0��ʼ�������·�Ϊmonth+1
						date.setText(year+"-"+(month+1)+"-"+day);}
					
				},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
				}
			});
			
		//�¼�������д�ú�Ϊ��ťע�ᵥ���¼�����
	    MyOnClickListener myOnClickListener =new MyOnClickListener ();
	        add.setOnClickListener(myOnClickListener);
	        query.setOnClickListener(myOnClickListener);
		}
			
	//ͨ�������࣬��ȡ���ݿ⣬������ӺͲ�ѯ����
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    mydbHelper = new MyDatabaseHelper(MainActivity.this,"memento.db",null,1);//�������ݿ⸨����
		    SQLiteDatabase db=mydbHelper.getReadableDatabase();//��ȡSQLite���ݿ�
			String subStr=subject.getText().toString();//��ȡ����༭�������
			String bodyStr=body.getText().toString();//��ȡ���ݱ༭�������
			String dateStr=date.getText().toString();//��ȡʱ��༭�������
			switch(v.getId()){
			case R.id.add://����������Ӱ�ť
				title.setVisibility(View.INVISIBLE);//���ñ�ͷ���ɼ�
				addMemento(db,subStr,bodyStr,dateStr);//������Ӽ�¼����
				Toast.makeText(MainActivity.this, "��ӱ���¼�ɹ���", 1000).show();
				result.setAdapter(null);//�����б�Ϊ��
				break;
			case R.id.query://�������ǲ�ѯ��ť
				title.setVisibility(View.VISIBLE);//���ñ�ͷ�ɼ�
				Cursor cursor=queryMemento(db,subStr,bodyStr,dateStr);//���ò�ѯ����
				SimpleCursorAdapter resultAdapter=new SimpleCursorAdapter(MainActivity.this,R.layout.result,cursor,
						new String[]{"_id","subject","body","date"},
						new int[]{R.id.memento_num,R.id.memento_subject,R.id.memento_body,R.id.memento_date});
				result.setAdapter(resultAdapter);//���������б������
				break;
			default:
				break;
			}
		}
	}

		

		public void addMemento(SQLiteDatabase db, String subject,String body, String date) {
			// TODO Auto-generated method stub
			db.execSQL("insert into memento_tb values(null,?,?,?)",new String[]{subject,body,date});//ִ�в������
			this.subject.setText("");//������ݺ󣬽����е��ı��༭���������Ϊ��
			this.body.setText("");
			this.date.setText("");			
		}
		
		public Cursor queryMemento(SQLiteDatabase db, String subject,String body, String date) {
			// TODO Auto-generated method stub
			Cursor cursor=db.rawQuery("select*from memento_tb where subject like?and body like? and date like?",
					new String[]{"%"+subject+"%","%"+body+"%","%"+date+"%"});//ִ�в�ѯ�������ṩģ����ѯ����
			return cursor;
		}
		protected void onDestroy() {
			if(mydbHelper!=null){
				mydbHelper.close();
			}
		}
	}