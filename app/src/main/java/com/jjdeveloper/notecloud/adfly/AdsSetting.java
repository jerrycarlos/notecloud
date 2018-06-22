package com.jjdeveloper.notecloud.adfly;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jjdeveloper.notecloud.config.Config;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.jjdeveloper.notecloud.config.Config.PREF_NAME_ADS;

public class AdsSetting {

    //Configuração

    //Ads Interstitial = Ads Full Screen que mostrar a propaganda
    private String uuidInterstitial = Config.uuidInterstitial; //SEU UUID

    //Ads RewardedVideoAd = Ads Video
    private String uuidRewardedVideoAd = Config.uuidRewardedVideoAd; //SEU UUID

    private Context mContext;
    private SharedPreferences sharedPreferences;

    public AdsSetting(Context context) {
        this.mContext = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME_ADS, MODE_PRIVATE);
        //MobileAds.initialize(mContext, Config.app_pub);
    }

    public void start() {


        //Se não quiser usar isso abaixo pra mostrar UM POR DIA apenas use

        loadInterstitialAd(); //para mostrar uma propaganda full screen
        //loadRewardedVideoAd(); //para mostrar um ads video


        /*if (sharedPreferences.contains("type") && sharedPreferences.contains("day")) {
            int type = sharedPreferences.getInt("type", 0);
            int day = sharedPreferences.getInt("day", 0);

            Calendar now = Calendar.getInstance();

            if (day != now.get(Calendar.DAY_OF_MONTH)) {
                if (type == 0) {
                    loadInterstitialAd();
                } else {
                    loadRewardedVideoAd();
                }
                applyType(type == 0 ? 1 : 0);
            }
            //Primeira propaganda normal full screen a ser exibida
        } else {
            applyType(0);
            setDayPreference();
            loadInterstitialAd();
        }*/
    }

    //Aqui é pra salvar o proximo tipo de propaganda a ser exibida
    // 0 = Interstitial (Normal Full Screen)
    // 1 = RewardedVideoAd (Video)
    private void applyType(int type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("type", type);
        editor.apply();
    }

    private void loadInterstitialAd() {
        final InterstitialAd mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(uuidInterstitial);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("F7FE7A31315748DB37FD0F08B7F254F9").build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //Exibir propaganda
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("errorCodeAds",String.valueOf(errorCode));
                //Falhou, vai recarregar
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("F7FE7A31315748DB37FD0F08B7F254F9").build());
            }

            @Override
            public void onAdClosed() {
                //SE QUISER MOSTRAR UMA PROPAGANDA POR DIA SÒ USA ESSA MINHA FUNÇÂO ABAIXO
                //Após fechar propaganda, atualizar os dias
                //setDayPreference();
            }
        });
    }

    private void loadRewardedVideoAd() {

        MobileAds.getRewardedVideoAdInstance(mContext);
        final RewardedVideoAd mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        mRewardedVideoAd.loadAd(uuidRewardedVideoAd, new AdRequest.Builder().addTestDevice("F7FE7A31315748DB37FD0F08B7F254F9").build());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                //Ads Video carregou vai ser exibida
                mRewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                setDayPreference();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                //Se deu algum erro na hora de carregar o video, vai tenta novamente...
                mRewardedVideoAd.loadAd(uuidRewardedVideoAd, new AdRequest.Builder().addTestDevice("F7FE7A31315748DB37FD0F08B7F254F9").build());
            }

        });
    }

    //Atualizar o dia depois de mostrar a propaganda
    private void setDayPreference() {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);//Obter dia do Mês
        SharedPreferences.Editor editor = sharedPreferences.edit(); //Salvar dia do Mês
        editor.putInt("day", day);
        editor.apply();
    }
}

