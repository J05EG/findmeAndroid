package com.example.findme.connections;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.models.MainUser;
import com.example.findme.models.Notify;
import com.example.findme.models.Person;
import com.example.findme.models.User;
import com.example.findme.models.UserDevice;
import com.example.findme.models.UserDeviceHome;
import com.example.findme.models.UserLogin;
import com.google.android.gms.maps.model.Circle;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectionBackend {
    String sql = "https://find-me-back-end.herokuapp.com/api/";
    Gson gson = new Gson();
    public final String LOGINOK = "Te logeaste con exito";

    public boolean LoginUser(final UserLogin login, Context context) throws IOException {
        String route = sql.concat("login");
        //setPolicy ();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        boolean checklogin = false;
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.connect();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("mail", login.getEmail());
            jsonParam.put("password", login.getPassword());
            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                MainUser usuario = new MainUser();
                String json = response.toString();
                JSONObject object = new JSONObject(json);

                for (int i = 0; i < object.length(); i++) {
                   try {
                       JSONObject jsonObject = object.getJSONObject("data");
                       usuario.setId(jsonObject.optString("id"));
                       usuario.setIdEmail(jsonObject.optString("MailId"));
                       usuario.setIdPerson(jsonObject.optString("PersonId"));
                       usuario.setIdMedioPago(jsonObject.optString("MedioPagoId"));
                       usuario.setPassword(login.getPassword());
                       mensaje= object.optString("menssage");
                   }catch (Exception e){
                       mensaje= object.optString("menssage");

                   }
                }
                if(ConstantSQLite.ConsultarDatosMainUser(context).getId() == null){
                    ConstantSQLite.RegisterUserMainSQL(usuario, context);
                }
                System.out.println(mensaje);
            Log.i("MSG", conn.getResponseMessage());
            if(conn.getResponseMessage().equals("OK")&&mensaje.equals(LOGINOK)){
                checklogin= true;
            }else{
                checklogin= false;
            }
            conn.disconnect();
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }
        return checklogin;
    }

    public User RegisterUser(final User registro) throws IOException {
        User usuario = new User();
        String route = sql.concat("newuser");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        boolean reg = false;
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("documentoIdentidad", registro.getRut());
            jsonParam.put("primerNombre", registro.getNombre());
            jsonParam.put("apellido", registro.getApellido());
            jsonParam.put("fechaNacimiento", registro.getfNacimiento());
            jsonParam.put("email", registro.getEmail());
            jsonParam.put("contra", registro.getPassword());

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i < object.length(); i++) {
                try {
                    JSONObject jsonObject = object.getJSONObject("data");
                    usuario.setId(jsonObject.optString("id"));
                    JSONObject jsonObject1 = object.getJSONObject("Person");
                    usuario.setRut(jsonObject1.optString("documentoIdentidad"));
                    usuario.setNombre(jsonObject1.optString("firstName"));
                    usuario.setApellido(jsonObject1.optString("lastName"));
                    usuario.setfNacimiento(jsonObject1.optString("fechaNacimiento"));
                    JSONObject jsonObject2 = object.getJSONObject("Mail");
                    usuario.setEmail(jsonObject2.optString("email"));
                    code =object.optString("code");
                }catch (Exception e){
                    mensaje= object.optString("menssage");
                    code = object.optString("code");
                }
            }
            System.out.println(mensaje);
            Log.i("MSG", conn.getResponseMessage());
            conn.disconnect();
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public UserDevice RegisterPerson(final Person registro, String UserId) throws IOException {
        UserDevice usuarioD = new UserDevice();
        String route = String.format(sql+"user/%s/createUserDevice",UserId );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("documentoIdentidad", registro.getRut());
            jsonParam.put("firstName", registro.getNombre());
            jsonParam.put("lastName", registro.getApellido());
            jsonParam.put("fechaNacimiento", registro.getFechaNacimiento());


            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);


            for (int i = 0; i < object.length(); i++) {
                try {
                    JSONObject jsonObject = object.getJSONObject("data");
                    usuarioD.setIdPersonDevice(jsonObject.optString("id"));
                    JSONObject jsonObject1 = jsonObject.getJSONObject("DatosPersona");
                    usuarioD.setDatosPersona(new Person(jsonObject1.optInt("id"), jsonObject1.optString("documentoIdentidad"),
                            jsonObject1.optString("firstName"),jsonObject1.optString("lastName"),
                            jsonObject1.optString("fechaNacimiento")));
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            System.out.println(mensaje);
            Log.i("MSG", conn.getResponseMessage());
            conn.disconnect();
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }
        return usuarioD;
    }

    public List<UserDevice> ListarUsuariosReceptor(String UserId){
        List<UserDevice> lista = new ArrayList<UserDevice>();
        String route = String.format(sql+"user/%s/UsersDevice",UserId );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            URL urlt = new URL(route);
            conn = (HttpURLConnection) urlt.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String json = response.toString();
            JSONArray object = new JSONArray(json);
            System.out.println("testing" + object.length());
            for (int i = 0; i < object.length(); i++) {
                JSONObject out = object.getJSONObject(i);
                    UserDevice user =  new UserDevice();
                    user.setIdPersonDevice(out.optString("id"));
                    JSONObject jsonObject = out.getJSONObject("DatosPersona");
                    user.setDatosPersona(new Person(jsonObject.optInt("id"), jsonObject.optString("documentoIdentidad"),jsonObject.optString("firstName"),jsonObject.optString("lastName"), jsonObject.optString("fechaNacimiento")));
                    lista.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("lista",lista.toString());
    return lista;
    }

    public List<UserDeviceHome> ListarUsuariosReceptorHome(String UserId){
        List<UserDeviceHome> lista = new ArrayList<UserDeviceHome>();
        String route = String.format(sql+"user/%s/UsersDevice",UserId );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            URL urlt = new URL(route);
            conn = (HttpURLConnection) urlt.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String json = response.toString();
            JSONArray object = new JSONArray(json);
            System.out.println("testing" + object.length());
            for (int i = 0; i < object.length(); i++) {
                JSONObject out = object.getJSONObject(i);
                UserDeviceHome user =  new UserDeviceHome();
                JSONObject jsonObject = out.getJSONObject("DatosPersona");
                user.setIdentificador(jsonObject.optString("firstName")+" "+jsonObject.optString("lastName"));
                if(out.getJSONObject("DatosMovil") != null){
                    JSONObject jsonObject1 = out.getJSONObject("DatosMovil");
                    JSONObject jsonObject11 = jsonObject1.getJSONObject("UbicacionActual");
                    user.setLatUbcacion(jsonObject11.getDouble("latitude"));
                    user.setLongUbicacion(jsonObject11.getDouble("longitude"));
                    if(out.getJSONObject("LimitArea")!= null){
                        JSONObject jsonObject2 = out.getJSONObject("LimitArea");
                        JSONObject jsonObject22 = jsonObject2.getJSONObject("LimitAreaLoc");
                        user.setRadio(jsonObject2.getDouble("Radio"));
                        user.setLatRadio(jsonObject22.getDouble("latitude"));
                        user.setLongRadio(jsonObject22.getDouble("longitude"));
                    }else{
                        user.setRadio(0);
                        user.setLatRadio(0);
                        user.setLongRadio(0);
                    }
                }else {
                    user.setLatUbcacion(0);
                    user.setLongUbicacion(0);
                }
                lista.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("lista",lista.toString());
        return lista;
    }

    public List<Notify> ListarNotificaicones(String UserId){
        List<Notify> lista = new ArrayList<Notify>();
        String route = String.format(sql+"user/%s/UsersDevice",UserId );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            URL urlt = new URL(route);
            conn = (HttpURLConnection) urlt.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String json = response.toString();
            JSONArray object = new JSONArray(json);
            System.out.println("testing" + object.length());
            for (int i = 0; i < object.length(); i++) {
                JSONObject out = object.getJSONObject(i);
                Notify notify = new Notify();
                //user.setIdPersonDevice(out.optString("id"));
                //JSONObject jsonObject = out.getJSONObject("DatosPersona");
                //user.setDatosPersona(new Person(jsonObject.optInt("id"), jsonObject.optString("documentoIdentidad"),jsonObject.optString("firstName"),jsonObject.optString("lastName"), jsonObject.optString("fechaNacimiento")));
                lista.add(notify);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("lista",lista.toString());
        return lista;
    }

    public User ConsultaDatosUserAPI(String userId){
        User user = new User();
        String route = String.format(sql+"users/"+userId);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            URL urlt = new URL(route);
            conn = (HttpURLConnection) urlt.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i< object.length();i++){
                JSONObject jsonObject= object.getJSONObject("Person");
                user.setRut(jsonObject.optString("documentoIdentidad"));
                user.setNombre(jsonObject.optString("firstName"));
                user.setApellido(jsonObject.optString("lastName"));
                user.setfNacimiento(jsonObject.optString("fechaNacimiento"));
            }
            System.out.println(user.getNombre());
            for (int i = 0; i< object.length();i++){
                JSONObject jsonObject= object.getJSONObject("Mail");
                user.setEmail(jsonObject.optString("email"));
            }
            return user;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void SetLimitArea(String userDeviceId , Circle circle) throws IOException  {
        String route = String.format(sql+"userdevice/"+userDeviceId+"/createlimitarea");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        double lat,longitud,radio;
        lat = circle.getCenter().latitude;
        longitud =circle.getCenter().longitude;
        radio = circle.getRadius();
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("lat", lat);
            jsonParam.put("longi", longitud);
            jsonParam.put("radio", radio);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i < object.length(); i++) {
                try {
                    JSONObject jsonObject = object.getJSONObject("mensaje");
                    mensaje = object.optString("mensaje");
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            System.out.println(mensaje);
            Log.i("MSG", mensaje);
            conn.disconnect();
        }catch (Exception e){
                e.getStackTrace();
        }
    }

    public String  getLimitAreaUser(String userDeviceId){
        String route = String.format(sql+"userdevice/"+userDeviceId+"/limitarea");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        String mensaje =null;
        try {
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Log.d("status",Integer.toString(conn.getResponseCode()));
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i< object.length();i++){
                JSONObject jsonObject= object.getJSONObject("data");
                String rad = jsonObject.optString("Radio");
                JSONObject jsonObject1 = jsonObject.getJSONObject("LimitAreaLoc");
                mensaje = String.format(jsonObject1.optString("latitude")+","+ jsonObject1.optString("longitude")+","+rad);
            }
            return  mensaje;
        }catch (Exception e){
            return "no se pudo localizar ";
        }
    }

    public String getLastUserLocation(String userDeviceId){
        String route = String.format(sql+"userdevice/"+userDeviceId+"/lastlocation");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        String mensaje =null;
        try {
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Log.d("status",Integer.toString(conn.getResponseCode()));
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i< object.length();i++){
                JSONObject jsonObject= object.getJSONObject("data");
                mensaje = String.format(jsonObject.optString("latitude")+","+ jsonObject.optString("longitude"));
            }
            return  mensaje;
        }catch (Exception e){
            return "no se pudo localizar ";
        }
    }

    public void setUseerSmartphone(String userId, String androidID){
        User user = new User();
        String route = String.format(sql+"user/"+userId+"/createsmartphone");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("androidId", androidID);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i < object.length(); i++) {
                try {
                    JSONObject jsonObject = object.getJSONObject("mensaje");
                    mensaje = object.optString("mensaje");
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            System.out.println(mensaje);
            Log.i("MSG", mensaje);


            conn.disconnect();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void updateSmartPhoneToken(String userId, String androidID){
        User user = new User();
        String route = String.format(sql+"user/"+userId+"/updatesmartphone");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("androidId", androidID);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i < object.length(); i++) {
                try {
                    JSONObject jsonObject = object.getJSONObject("mensaje");
                    mensaje = object.optString("mensaje");
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            System.out.println(mensaje);
            Log.i("MSG", mensaje);


            conn.disconnect();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void deleteUserDevice(String userDeviceId){
        User user = new User();
        String route = String.format(sql+"userdevice/"+userDeviceId);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn;
        try {
            String mensaje ="",code = "";
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = response.toString();
            JSONObject object = new JSONObject(json);
            for (int i = 0; i < object.length(); i++) {
                try {
                    JSONObject jsonObject = object.getJSONObject("mensaje");
                    mensaje = object.optString("mensaje");
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            System.out.println(mensaje);
            Log.i("MSG", mensaje);


            conn.disconnect();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */

}


