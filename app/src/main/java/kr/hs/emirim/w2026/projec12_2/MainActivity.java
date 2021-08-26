package kr.hs.emirim.w2026.projec12_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyDBHelper dbHelper;
    EditText editName, editCount, editResultN,editResultC;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editResultN = findViewById(R.id.edit_name_result);
        editResultC = findViewById(R.id.edit_count_result);
        Button btnInit = findViewById(R.id.btn_init);
        Button btnInput = findViewById(R.id.btn_input);
        Button btnEdit = findViewById(R.id.btn_edit);
        Button btnDelete = findViewById(R.id.btn_delete);
        Button btnSearch = findViewById(R.id.btn_search);


        dbHelper = new MyDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                dbHelper.onUpgrade(db,1,2);
                db.close();
                search();
            }
        });
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db= dbHelper.getWritableDatabase();
                db.execSQL("insert into groupTB values('"+editName.getText().toString()+"',"+editCount.getText().toString()+");");
                db.close();
                search();
                Toast.makeText(getApplicationContext(), "정상적으로 입력되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                db.execSQL("update groupTB set count="+ Integer.parseInt(editCount.getText().toString())+" where name = '"+ editName.getText().toString() + "';");
                search();
                db.close();
            }
        });
        // 삭제
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM groupTB WHERE name= '"+ editName.getText().toString()+"';");
                search();
                db.close();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }
    public void search(){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from groupTB;",null);
        String strName = "그룹 이름\r\n_________\r\n";
        String strCount = "인원수\r\n_________\r\n";
        while(cursor.moveToNext()){
            strName +=cursor.getString(0)+"\r\n";
            strCount += cursor.getInt(1)+"\r\n";
        }
        editResultN.setText(strName);
        editResultC.setText(strCount);

        cursor.close();
        db.close();
    }


    public class MyDBHelper extends SQLiteOpenHelper{

        public MyDBHelper(Context context){
            super(context, "groupDB",null,1);

        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE table groupTB (name char(20) primary key, count Integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists groubTB");
            onCreate(db);
        }
    }
}