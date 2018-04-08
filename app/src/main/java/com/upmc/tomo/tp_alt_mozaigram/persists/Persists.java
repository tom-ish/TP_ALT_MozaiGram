package com.upmc.tomo.tp_alt_mozaigram.persists;

import com.upmc.tomo.tp_alt_mozaigram.model.User;

/**
 * Created by Tomo on 20/02/2018.
 */

public class Persists {
    public static int PICK_IMAGE_REQUEST = 1;
    public static final String APP_IMAGES_STORAGE_DIR_PATH = "MozaiGram";
    public static final String APP_TILES_BANK_DIR_PATH = "Pokemon";
    public static final String APP_SIGNATURE = "mozaik_";
    public static final String APP_TILE_SIGNATURE = "tile_";
    public static final String IMG_EXTENSION = ".jpg";
    public static final String SELECTED_MOZAIK_PATH = "path";
    public static final Integer PERMISSIONS = 200;
    public static final Integer CAMERA = 10;
    public static final Integer GALLERY = 20;

    public static final String GENERATE_MOZAIK_FRAGMENT_TAG = "MozaikGenerationFragment";
    public static final String GALLERY_FRAGMENT_TAG = "GalleryFragment";

    public static String REQUEST_TAG = "com.upmc.tomo.TP_ALT_MozaiGram";
    public static User currentUser;

    public static String POKEMON_IMAGE_REFERENCE_PATH = "C:\\Users\\Tomo\\Documents\\TP_ALT_MozaiGram\\pokemon_list.png";

}
