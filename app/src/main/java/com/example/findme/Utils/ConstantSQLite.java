package com.example.findme.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.findme.connections.ConectionSQLite;
import com.example.findme.models.MainUser;
import com.example.findme.models.User;
import com.example.findme.models.UserLogin;

public class ConstantSQLite {
    public static final String BASE_DATOS = "db_findme";
    public static final String TABLA_USUARIO = "user";
    public static final String TABLA_MAIN_USER = "main";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_MAIL_ID = "mailId";
    public static final String CAMPO_PERSON_ID = "personId";
    public static final String CAMPO_RUT = "rut";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_APELLIDO = "apellido";
    public static final String CAMPO_FNACIMIENTO = "fechaNacimiento";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_PASSWORD = "password";
    public static final String CAMPO_MEDIO_PAGO = "idPago";
    public static final String CAMPO_TOKEN = "token";
    public static final String CREAR_TABLA_USUARIO = "CREATE TABLE "+TABLA_USUARIO+" ("+CAMPO_ID+" INTEGER, "+CAMPO_RUT+" TEXT, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_APELLIDO+" TEXT, "+CAMPO_FNACIMIENTO+ " TEXT, "+CAMPO_EMAIL+" TEXT, "+CAMPO_PASSWORD+" TEXT, "+CAMPO_MEDIO_PAGO+" TEXT, "+CAMPO_TOKEN+" TEXT)";

    public static final String CREAR_TABLA_MAIN = "CREATE TABLE "+TABLA_MAIN_USER+" ("+CAMPO_ID+" INTEGER, "+CAMPO_MAIL_ID+" INTEGER, "+CAMPO_PERSON_ID+" INTEGER, "+CAMPO_PASSWORD+" TEXT, "+CAMPO_MEDIO_PAGO+ " INTEGER)";

    public static void RegisterUserMainSQL(MainUser user, Context context) {
        ConectionSQLite conSQL;
        conSQL = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = conSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantSQLite.CAMPO_ID, user.getId());
        values.put(ConstantSQLite.CAMPO_MAIL_ID, user.getIdEmail());
        values.put(ConstantSQLite.CAMPO_PERSON_ID, user.getIdPerson());
        values.put(ConstantSQLite.CAMPO_PASSWORD, user.getPassword());
        values.put(ConstantSQLite.CAMPO_MEDIO_PAGO, user.getIdMedioPago());
        db.insert(ConstantSQLite.TABLA_MAIN_USER, null, values);
        db.close();
    }

    public static void RegisterUserSQL(User user, Context context) {
        //context.deleteDatabase(BASE_DATOS);
        ConectionSQLite conSQL;
        conSQL = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = conSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantSQLite.CAMPO_ID, ConsultarDatosMainUser(context).getId());
        values.put(ConstantSQLite.CAMPO_RUT, user.getRut());
        values.put(ConstantSQLite.CAMPO_NOMBRE, user.getNombre());
        values.put(ConstantSQLite.CAMPO_APELLIDO, user.getApellido());
        values.put(ConstantSQLite.CAMPO_FNACIMIENTO, user.getfNacimiento());
        values.put(ConstantSQLite.CAMPO_EMAIL, user.getEmail());
        values.put(ConstantSQLite.CAMPO_PASSWORD, ConsultarDatosMainUser(context).getPassword());
        values.put(ConstantSQLite.CAMPO_TOKEN, user.getToken());
        db.insert(ConstantSQLite.TABLA_USUARIO, null, values);
        db.close();
    }

    public static UserLogin ConsultarDatosUserLogin(Context context) {
        UserLogin user = new UserLogin();
        ConectionSQLite conSQLite = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = conSQLite.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT "+ConstantSQLite.CAMPO_EMAIL+", "+ConstantSQLite.CAMPO_PASSWORD+" FROM "+ConstantSQLite.TABLA_USUARIO, null);
            cursor.moveToFirst();
            user.setEmail(cursor.getString(cursor.getColumnIndex(ConstantSQLite.CAMPO_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(ConstantSQLite.CAMPO_PASSWORD)));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context, "No se pudo obtener login", Toast.LENGTH_SHORT).show();
        }
        db.close();
        return user;
    }

    public static MainUser ConsultarDatosMainUser(Context context) {
        MainUser mainUser = new MainUser();
        ConectionSQLite conSQLite = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = conSQLite.getReadableDatabase();
        try {
            Cursor cursorMain = db.rawQuery("SELECT "+ConstantSQLite.CAMPO_ID+", "+ConstantSQLite.CAMPO_PASSWORD+" FROM "+ConstantSQLite.TABLA_MAIN_USER, null);
            cursorMain.moveToFirst();
            mainUser.setId(cursorMain.getString(cursorMain.getColumnIndex(CAMPO_ID)));
            mainUser.setPassword(cursorMain.getString(cursorMain.getColumnIndex(CAMPO_PASSWORD)));
            cursorMain.close();
        }catch (Exception e){
            Toast.makeText(context, "No se puede obtener usuario main", Toast.LENGTH_SHORT).show();
        }
        db.close();
        return mainUser;
    }

    public static User ConsultarDatosUser(Context context) {
        User user = new User();
        ConectionSQLite conSQLite = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = conSQLite.getReadableDatabase();
        try {
            Cursor cursorUser = db.rawQuery("SELECT "+CAMPO_ID+", "+CAMPO_NOMBRE+", "+CAMPO_APELLIDO+", " +
                    ""+CAMPO_TOKEN+", "+CAMPO_RUT+", "+CAMPO_EMAIL+", "+CAMPO_FNACIMIENTO+" FROM "+TABLA_USUARIO, null);
            cursorUser.moveToFirst();
            user.setId(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_ID)));
            user.setNombre(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_NOMBRE)));
            user.setApellido(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_APELLIDO)));
            user.setEmail(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_EMAIL)));
            user.setfNacimiento(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_FNACIMIENTO)));
            user.setRut(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_RUT)));
            user.setToken(cursorUser.getString(cursorUser.getColumnIndex(CAMPO_TOKEN)));
            cursorUser.close();
        }catch (Exception e){
            Toast.makeText(context, "No se pudo obtener usuario", Toast.LENGTH_SHORT).show();
        }
        db.close();
        return user;
    }

    public static void UpdateTokenUser(Context context, String token, String id){
        ConectionSQLite conSQL;
        conSQL = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = conSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_TOKEN,token);
        db.update(TABLA_USUARIO,values,String.format(CAMPO_ID+" = "+ id),null );
        db.close();
    }

    public static void BorrarDB(Context context){
        ConectionSQLite sqLite = new ConectionSQLite(context, ConstantSQLite.BASE_DATOS, null, 1);
        SQLiteDatabase db = sqLite.getWritableDatabase();
        sqLite.deleteAll(db);
        db.close();
    }
}
