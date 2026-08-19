package com.teknobirader.philipshome;

import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.*;

public class LauncherView extends View {
    private final Context ctx;
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final ArrayList<AppItem> apps = new ArrayList<>();
    private int selected = 0;
    private final float d;
    static class AppItem { String label; Drawable icon; Intent launch; }

    public LauncherView(Context c) {
        super(c); ctx=c; d=getResources().getDisplayMetrics().density;
        setFocusable(true); setFocusableInTouchMode(true); refreshApps();
    }
    public void refreshApps(){
        apps.clear(); PackageManager pm=ctx.getPackageManager();
        Intent q=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> list=pm.queryIntentActivities(q,0);
        Collections.sort(list,new ResolveInfo.DisplayNameComparator(pm));
        for(ResolveInfo r:list){
            if(r.activityInfo.packageName.equals(ctx.getPackageName())) continue;
            AppItem a=new AppItem(); a.label=String.valueOf(r.loadLabel(pm)); a.icon=r.loadIcon(pm);
            a.launch=pm.getLeanbackLaunchIntentForPackage(r.activityInfo.packageName);
            if(a.launch==null)a.launch=pm.getLaunchIntentForPackage(r.activityInfo.packageName);
            apps.add(a);
        }
        if(selected>=apps.size())selected=Math.max(0,apps.size()-1); invalidate();
    }
    private float dp(float x){return x*d;}
    private void txt(Canvas c,String s,float x,float y,float z,boolean bold){p.setShader(null);p.setColor(Color.WHITE);p.setTextSize(dp(z));p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,x,y,p);}
    private void box(Canvas c,float l,float t,float r,float b,int color){p.setShader(null);p.setColor(color);c.drawRoundRect(l,t,r,b,dp(16),dp(16),p);}
    @Override protected void onDraw(Canvas c){
        int w=getWidth(); c.drawColor(Color.rgb(4,7,15));
        LinearGradient g=new LinearGradient(0,0,w,0,new int[]{Color.rgb(8,26,58),Color.rgb(4,7,15)},null,Shader.TileMode.CLAMP);p.setShader(g);c.drawRect(0,0,w,getHeight(),p);p.setShader(null);
        txt(c,"TEKNOBİRADER",dp(38),dp(52),24,true); txt(c,"PHILIPS TV HOME",dp(40),dp(76),11,true);
        String time=new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()); txt(c,time,w-dp(125),dp(52),26,true);
        box(c,dp(38),dp(110),w-dp(38),dp(275),Color.rgb(12,32,66)); txt(c,"Philips için özel ana ekran",dp(72),dp(165),27,true); txt(c,"Hızlı • Reklamsız • Türkçe • Kumanda ile kullanım",dp(72),dp(205),14,false);
        txt(c,"Uygulamalar",dp(40),dp(330),18,true);
        int n=Math.min(12,apps.size()); float gap=dp(14), left=dp(40), right=w-dp(40), cw=(right-left-gap*5)/6f, ch=dp(105);
        for(int i=0;i<n;i++){
            int row=i/6,col=i%6;float l=left+col*(cw+gap),t=dp(355)+row*(ch+gap); box(c,l,t,l+cw,t+ch,i==selected?Color.rgb(36,92,210):Color.rgb(15,24,43));
            AppItem a=apps.get(i); if(a.icon!=null){int s=(int)dp(48),cx=(int)(l+cw/2),iy=(int)(t+dp(10));a.icon.setBounds(cx-s/2,iy,cx+s/2,iy+s);a.icon.draw(c);} p.setTextAlign(Paint.Align.CENTER);String name=a.label.length()>17?a.label.substring(0,16)+"…":a.label;txt(c,name,l+cw/2,t+dp(88),11,i==selected);p.setTextAlign(Paint.Align.LEFT);
        }
        txt(c,"58PUS8507/62 • Android TV • D-Pad",dp(40),getHeight()-dp(30),11,false);
    }
    @Override public boolean onKeyDown(int k,KeyEvent e){
        int n=Math.min(12,apps.size()); if(n==0)return super.onKeyDown(k,e);
        if(k==KeyEvent.KEYCODE_DPAD_LEFT&&selected%6>0)selected--;
        else if(k==KeyEvent.KEYCODE_DPAD_RIGHT&&selected+1<n&&selected%6<5)selected++;
        else if(k==KeyEvent.KEYCODE_DPAD_UP&&selected>=6)selected-=6;
        else if(k==KeyEvent.KEYCODE_DPAD_DOWN&&selected+6<n)selected+=6;
        else if(k==KeyEvent.KEYCODE_DPAD_CENTER||k==KeyEvent.KEYCODE_ENTER){Intent i=apps.get(selected).launch;if(i!=null)try{ctx.startActivity(i);}catch(Exception x){Toast.makeText(ctx,"Uygulama açılamadı",Toast.LENGTH_SHORT).show();}return true;} else return super.onKeyDown(k,e);
        invalidate();return true;
    }
}
