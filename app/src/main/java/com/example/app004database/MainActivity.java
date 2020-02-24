package com.example.app004database;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView mResult;
    private Button mInsert;
    private Button mRead;
    private Button mAllDelete;
    private EditText mKey;
    private EditText mValue;

    private TestOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKey = findViewById(R.id.etKey);
        mValue = findViewById(R.id.etValue);
        mResult = findViewById(R.id.tvResult);
        mInsert = findViewById(R.id.btInsert);
        mInsert.setOnClickListener(this);
        mRead = findViewById(R.id.btRead);
        mRead.setOnClickListener(this);
        mAllDelete = findViewById(R.id.btAllDelete);
        mAllDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btInsert:
                if (mOpenHelper == null) {
                    // 生成.
                    mOpenHelper = new TestOpenHelper(getApplicationContext());
                }
                if (mDatabase == null) {
                    // 読み書き両用のインスタンスを生成.
                    mDatabase = mOpenHelper.getWritableDatabase();
                }
                String number = mKey.getText().toString();
                String name = mValue.getText().toString();

                if (number.length() == 0) {
                    Toast.makeText(this, "[ No. ]が未入力です。", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (name.length() == 0) {
                    Toast.makeText(this, "[ Name ]が未入力です。", Toast.LENGTH_SHORT).show();
                    break;
                }
                // データ登録.
                insertData(mDatabase, number, name);
                break;

            case R.id.btRead:
                // データ読み出し.
                readData();
                break;

            case R.id.btAllDelete:
                // テーブル削除.
                deleteAllData(mDatabase);
                break;
        }
    }

    /**
     * データ登録.
     *
     * @param database
     * @param key
     * @param value
     */
    private void insertData(SQLiteDatabase database, String key, String value) {
        ContentValues values = new ContentValues();
        values.put(TestOpenHelper.COLUMN_NAME_TITLE, key);
        values.put(TestOpenHelper.COLUMN_NAME_SUB_TITLE, value);

        long id = database.insert(TestOpenHelper.TABLE_NAME, null, values);
        if (id != -1) {
            Toast.makeText(this, "登録しました。", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No." + key + "はすでに登録済みです。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * データ読み出し.
     */
    private void readData() {
        if (mOpenHelper == null) {
            // 生成.
            mOpenHelper = new TestOpenHelper(getApplicationContext());
        }
        if (mDatabase == null) {
            // 読み専用のインスタンスを生成.
            mDatabase = mOpenHelper.getReadableDatabase();
        }
        Cursor cursor = mDatabase.query(
                TestOpenHelper.TABLE_NAME,
                new String[]{TestOpenHelper.COLUMN_NAME_TITLE,
                        TestOpenHelper.COLUMN_NAME_SUB_TITLE},
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder stringBuilder = new StringBuilder();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                stringBuilder.append(cursor.getInt(0));
                stringBuilder.append(": ");
                stringBuilder.append(cursor.getString(1));
                stringBuilder.append("\n");
                cursor.moveToNext();
            }
            cursor.close();
            mResult.setText(stringBuilder.toString());
        } else {
            // 取得したレコード件数が0件の場合.
            Toast.makeText(this, "登録されたデータはございません。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 全データ削除.
     *
     * @param database
     */
    private void deleteAllData(final SQLiteDatabase database) {
        // ダイアログ生成.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("データをすべて削除します。\n" + "よろしいでしょうか？");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (database != null) {
                    // データ全削除.
                    database.delete(TestOpenHelper.TABLE_NAME, null, null);
                }
                // 表示初期化.
                mResult.setText("");
                mKey.setText("");
                mValue.setText("");
            }
        });
        dialog.setNegativeButton("NG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしない.
            }
        });
        dialog.show();
    }
}
