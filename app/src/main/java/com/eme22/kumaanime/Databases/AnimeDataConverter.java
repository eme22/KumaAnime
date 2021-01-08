package com.eme22.kumaanime.Databases;

import androidx.room.TypeConverter;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;

public class AnimeDataConverter {


    @TypeConverter
    public static String MainPictureToString(MainPicture picture) {

        String medium = "",large = "";

        try {
             medium = picture.getMedium();
        }
        catch (NullPointerException ignored){ }
        try{
            large = picture.getLarge();
        }
        catch (NullPointerException ignored) { }


            return medium+","+large;


    }

    @TypeConverter
    public static MainPicture StringToMainPicture(String main) {
        MainPicture main2 = new MainPicture();
        String[] resources = main.split(",");
        main2.setMedium(resources[0]);
        main2.setLarge(resources[1]);
        return main2;
    }


}
