package com.rkadmin.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private final int navy = Color.rgb(18, 29, 54);
    private final int blue = Color.rgb(48, 105, 220);
    private LinearLayout root;
    private final ArrayList<String> users = new ArrayList<>();
    private boolean loggedIn = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(navy);
        getWindow().setNavigationBarColor(navy);
        showLogin();
    }

    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(radius); return d;
    }
    private TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size);
        t.setTextColor(color); if (bold) t.setTypeface(null, Typeface.BOLD);
        return t;
    }
    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    private LinearLayout row() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL); return l;
    }
    private void base(String title) {
        root = column(); root.setBackgroundColor(Color.rgb(245,247,251));
        setContentView(root);
        TextView top = text(title, 21, Color.WHITE, true);
        top.setPadding(22, 22, 18, 22); top.setBackgroundColor(navy);
        root.addView(top, new LinearLayout.LayoutParams(-1, -2));
    }
    private void showLogin() {
        base("RK ADMIN");
        LinearLayout card = column(); card.setPadding(22, 24, 22, 24);
        card.setBackground(bg(Color.WHITE, 24));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.setMargins(20, 70, 20, 20); root.addView(card, cp);
        TextView heading = text("Welcome back", 25, navy, true);
        card.addView(heading);
        TextView sub = text("Sign in to your admin dashboard", 14, Color.GRAY, false);
        sub.setPadding(0,8,0,24); card.addView(sub);
        EditText email = new EditText(this); email.setSingleLine(true);
        email.setHint("Admin username"); email.setText("admin");
        card.addView(email);
        EditText pass = new EditText(this); pass.setSingleLine(true);
        pass.setHint("Password"); pass.setInputType(129); pass.setText("admin123");
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1,-2); pp.topMargin=10;
        card.addView(pass, pp);
        Button login = button("LOGIN");
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1,54); bp.topMargin=22;
        card.addView(login,bp);
        TextView hint = text("Demo login: admin / admin123", 12, Color.GRAY, false);
        hint.setPadding(0,18,0,0); card.addView(hint);
        login.setOnClickListener(v -> {
            if ("admin".contentEquals(email.getText().toString().trim()) &&
                "admin123".contentEquals(pass.getText().toString())) {
                loggedIn = true; showDashboard();
            } else new AlertDialog.Builder(this).setMessage("Incorrect username or password").setPositiveButton("OK",null).show();
        });
    }
    private Button button(String label) {
        Button b = new Button(this); b.setText(label); b.setTextColor(Color.WHITE);
        b.setAllCaps(false); b.setTextSize(15); b.setBackground(bg(blue,16)); return b;
    }
    private void showDashboard() {
        base("RK ADMIN   •   Dashboard");
        ScrollView scroll = new ScrollView(this); LinearLayout content=column();
        content.setPadding(18,18,18,22); scroll.addView(content); root.addView(scroll);
        TextView welcome=text("Overview",23,navy,true); content.addView(welcome);
        TextView desc=text("Manage your app from one place.",14,Color.GRAY,false);
        desc.setPadding(0,5,0,18); content.addView(desc);
        LinearLayout stats=row();
        stats.addView(statCard("TOTAL USERS",String.valueOf(users.size())),new LinearLayout.LayoutParams(0,112,1));
        View gap=new View(this); stats.addView(gap,new LinearLayout.LayoutParams(12,1));
        stats.addView(statCard("STATUS","Active"),new LinearLayout.LayoutParams(0,112,1));
        content.addView(stats);
        addMenu(content,"👥  User Management","Add, view and remove users",v->showUsers());
        addMenu(content,"⚙  Settings","Admin and app settings",v->showSettings());
        addMenu(content,"↪  Logout","Return to the login screen",v->{loggedIn=false;showLogin();});
        TextView note=text("RK ADMIN  •  Version 1.0",12,Color.GRAY,false);
        note.setGravity(Gravity.CENTER); note.setPadding(0,25,0,0); content.addView(note);
    }
    private View statCard(String label,String value) {
        LinearLayout c=column(); c.setPadding(14,14,10,10); c.setBackground(bg(Color.WHITE,18));
        c.addView(text(label,11,Color.GRAY,true));
        TextView v=text(value,23,navy,true); v.setPadding(0,9,0,0); c.addView(v); return c;
    }
    private void addMenu(LinearLayout parent,String title,String sub,View.OnClickListener click) {
        LinearLayout c=column(); c.setPadding(17,15,17,15); c.setBackground(bg(Color.WHITE,18));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.topMargin=13;
        parent.addView(c,p); c.addView(text(title,17,navy,true));
        TextView st=text(sub,13,Color.GRAY,false); st.setPadding(0,5,0,0); c.addView(st);
        c.setOnClickListener(click);
    }
    private void showUsers() {
        base("RK ADMIN   •   Users");
        LinearLayout body=column(); body.setPadding(18,18,18,18); root.addView(body);
        Button add=button("+ Add User"); body.addView(add);
        add.setOnClickListener(v->{
            EditText input=new EditText(this); input.setHint("Enter user name");
            new AlertDialog.Builder(this).setTitle("New user").setView(input)
                .setPositiveButton("Add",(d,w)->{
                    String name=input.getText().toString().trim();
                    if(!name.isEmpty()){users.add(name);showUsers();}
                }).setNegativeButton("Cancel",null).show();
        });
        if(users.isEmpty()) {
            TextView empty=text("No users added yet. Tap “Add User” to create one.",14,Color.GRAY,false);
            empty.setPadding(0,25,0,0); body.addView(empty);
        }
        for(int i=0;i<users.size();i++){
            final int index=i;
            LinearLayout item=row(); item.setPadding(14,12,8,12);
            item.setBackground(bg(Color.WHITE,14));
            LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,-2); ip.topMargin=10; body.addView(item,ip);
            TextView name=text(users.get(i),16,navy,true); item.addView(name,new LinearLayout.LayoutParams(0,-2,1));
            Button del=new Button(this); del.setText("Remove"); del.setTextSize(12); del.setAllCaps(false);
            item.addView(del); del.setOnClickListener(v->new AlertDialog.Builder(this)
                .setMessage("Remove "+users.get(index)+"?")
                .setPositiveButton("Remove",(d,w)->{users.remove(index);showUsers();})
                .setNegativeButton("Cancel",null).show());
        }
        Button back=button("← Back to Dashboard");
        LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,52); bp.topMargin=20; body.addView(back,bp);
        back.setOnClickListener(v->showDashboard());
    }
    private void showSettings() {
        base("RK ADMIN   •   Settings");
        LinearLayout body=column(); body.setPadding(20,22,20,20); root.addView(body);
        body.addView(text("App Settings",22,navy,true));
        TextView info=text("App name: RK ADMIN\nVersion: 1.0\nAccount: admin\n\nThis is an offline demo dashboard. User data is kept in memory and resets when the app is closed.",15,Color.DKGRAY,false);
        info.setPadding(0,18,0,25); body.addView(info);
        Button back=button("← Back to Dashboard"); body.addView(back);
        back.setOnClickListener(v->showDashboard());
    }
}
