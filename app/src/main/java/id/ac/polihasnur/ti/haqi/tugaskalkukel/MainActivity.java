package id.ac.polihasnur.ti.haqi.tugaskalkukel;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView txHasil, txAngka;
    String container = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txHasil = findViewById(R.id.txHasil);
        txAngka = findViewById(R.id.txAngka);
    }

    public void nol(View view) {
        container = container + "0";
        txAngka.setText(container);
    }

    public void satu(View view) {
        container = container + "1";
        txAngka.setText(container);
    }

    public void dua(View view) {
        container = container + "2";
        txAngka.setText(container);
    }

    public void tiga(View view) {
        container = container + "3";
        txAngka.setText(container);
    }

    public void empat(View view) {
        container = container + "4";
        txAngka.setText(container);
    }

    public void lima(View view) {
        container = container + "5";
        txAngka.setText(container);
    }

    public void enam(View view) {
        container = container + "6";
        txAngka.setText(container);
    }

    public void tujuh(View view) {
        container = container + "7";
        txAngka.setText(container);
    }

    public void delapan(View view) {
        container = container + "8";
        txAngka.setText(container);
    }

    public void sembilan(View view) {
        container = container + "9";
        txAngka.setText(container);
    }

    public void allClear(View view) {
        txHasil.setText("0");
        container = "";
        txAngka.setText("");
    }

    public void plusMinus(View view) {
        container = "(-" + container + ")";
        txAngka.setText(container);
    }

    public void dot(View view) {
        container = container + ".";
        txAngka.setText(container);
    }

    public void delete(View view) {
        if (container.length() > 0) {
            container = container.substring(0, container.length() - 1);
            txAngka.setText(container);
        }
    }

    public void tambah(View view) {
        container = container + " + ";
        txAngka.setText(container);
    }

    public void kurang(View view) {
        container = container + " - ";
        txAngka.setText(container);
    }

    public void bagi(View view) {
        container = container + " / ";
        txAngka.setText(container);
    }

    public void kali(View view) {
        container = container + " * ";
        txAngka.setText(container);
    }

    public void modulus(View view) {
        container = container + " mod ";
        txAngka.setText(container);
    }

    public void samadengan(View view) {
        txHasil.setText(eval(container) + "");
        container = "";
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else if (eat('m')) { // modulus
                        nextChar(); // eat 'o'
                        nextChar(); // eat 'd'
                        x %= parseFactor();
                    } else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
