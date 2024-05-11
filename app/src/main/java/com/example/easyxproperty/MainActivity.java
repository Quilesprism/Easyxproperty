package com.example.easyxproperty;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class MainActivity extends AppCompatActivity {
    private final String encodedText="llave_secreta_primaria";
    String  SECRET_KEY= Base64.encodeToString(encodedText.getBytes(), Base64.DEFAULT);
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextCuenta);
        editTextPassword = findViewById(R.id.editTextPIN);
        buttonLogin = findViewById(R.id.buttonIngresar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Verifica el usuario y la contraseña (aquí deberías implementar tu lógica de autenticación)

                // Si la autenticación es exitosa, genera el token JWT
                String token = generateToken(username);
                Toast.makeText(MainActivity.this, "Token JWT: " + token, Toast.LENGTH_LONG).show();
                decoded(token);
            }
        });
    }

    private String generateToken(String username) {


        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        // Crea el token JWT
        String token = Jwts.builder()
                .subject(username)
                .signWith(key)
                .compact();

        return token;
    }
    private void decodeToken(String token) {
        // Decodifica el token
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            Toast.makeText(MainActivity.this, "JWT Decodificado, Nombre de usuario: " + username, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
        }
    }
    public void decoded(String JWTEncoded){
        try {
            String[] split = JWTEncoded.split("\\.");
            Toast.makeText(MainActivity.this, "JWT Decodificado, Nombre de usuario: " + getJson(split[1]), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
    }
        }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

}
