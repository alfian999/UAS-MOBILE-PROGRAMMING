package com.bc181.ardiansyah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_film";
    private final static String KEY_JUDUl = "Judul";
    private final static String KEY_TAHUN = "Tahun";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_PEMAIN = "Pemain";
    private final static String KEY_SIPNOSIS= "SIPNOSIS";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    private Context context;


    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FILM = "CREATE TABLE " +  TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUl + " TEXT, " + KEY_TAHUN + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_GENRE + " TEXT, "
                + KEY_PEMAIN + " TEXT, " + KEY_SIPNOSIS + " TEXT);";
        db.execSQL(CREATE_TABLE_FILM);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());


        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());

        db.insert(TABLE_FILM, null, cv);

    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUl, dataFilm.getJudul());
        cv.put(KEY_TAHUN, sdFormat.format(dataFilm.getTahun()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_GENRE, dataFilm.getGenre());
        cv.put(KEY_PEMAIN, dataFilm.getPemain());
        cv.put(KEY_SIPNOSIS, dataFilm.getSipnosis());


        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm())});
        db.close();
    }

    public void hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if(csr.moveToFirst()){
            do {
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                }
                catch (ParseException er){
                    er.printStackTrace();
                }

                Film tempFilm = new Film(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }
        return dataFilm;
    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //menambah data Film 1
        try{
            tempDate = sdFormat.parse("2019");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film1 = new Film(
                idFilm,
                "Rumput Tetangga",
                tempDate,
                storeImageFile(R.drawable.film1),
                "comedy, romantic",
                "Titi Kamal (Kirana), Donita (Diana), Gading Marten (Indra), Tora Sudiro (Tamtama)",
                "Tak jarang, orang merasa hal yang dia inginkan jauh lebih penting ketimbang hal yang dia butuhkan. Jika mendapatkan kesempatan memilih, mana yang akan Anda kejar? Hal serupa terjadi pada Kirana (Titi Kamal) dalam film \"Rumput Tetangga\". Kirana begitu populer dan cerdas semasa duduk di sekolah menengah atas. Impiannya kala itu menjadi konsultan public relations yang sukses. Namun kenyataannya, berbeda dari harapan Kirana. Dia menikah setelah kuliah dengan Ben (Raffi Ahmad) dan mendapatkan dua buah hati Rega (Daffa Deddy) dan Windy (Aqila Herby). Impiannya pupus karena dia terlalu sibuk menjani peran sebagai ibu rumah tangga."
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data Film ke 2
        try{
            tempDate = sdFormat.parse("2017");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film(
                idFilm,
                "Jailangkung",
                tempDate,
                storeImageFile(R.drawable.film2),
                "Horor",
                "Amanda Rawles (Bella), Jefri Nichol (Rama), Hannah Al Rashid (Angel)",
                "Bella, anak kedua dari pengusaha tambang Ferdi, ingin menguak misteri yang dialami ayahnya. Seminggu yang lalu, Ferdi ditemukan tak sadarkan diri oleh Kapten Wardana di sebuah vila di Pulau Alamkramat yang terletak di Jawa Timur. Secara medis, Ferdi sehat-sehat saja, namun dia tetap tak kunjung bangun. Bella meminta bantuan dari Rama, seorang mahasiswa yang tertarik pada hal metafisika. Bersama dengan kakak dan adik Bella, Angel dan Tasya, mereka berdua menuju ke Alamkramat. \n" +
                        "Di Alamkramat, Bella mengetahui bahwa selama enam bulan terakhir, Ferdi telah menggunakan sebuah jailangkung untuk berkomunikasi dengan mendiang istrinya, Sarah. Setelah beberapa kali berkomunikasi, Sarah memohon Ferdi untuk tidak memanggilnya lagi, karena bila dia melakukannya, dia akan memanggil makhluk gaib bernama Matianak. Ferdi tetap keras kepala dan pada akhirnya memanggil Matianak. Rama menduga bahwa Matianak telah menempel pada Ferdi; selama dia tidak dikembalikan ke tempatnya, Ferdi tidak akan bisa bangun. Bella, Rama, dan Angel memainkan jailangkung supaya Matianak dapat kembali. Alih-alih terkembalikan, Matianak menyerang Tasya dan membuatnya tak sadarkan diri, persis seperti Ferdi\n");
        tambahFilm(film2, db);
        idFilm++;

        // Tambah Film 3

        try{
            tempDate = sdFormat.parse("2018");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film(
                idFilm,
                "Sabrina",
                tempDate,
                storeImageFile(R.drawable.film3),
                "Horor",
                "Luna Maya, Christian Sugiono, Jeremy Thomas, dan Sara Wijayanto",
                "fılm Sabrina menceritakan tentang maira yang hidup bahagia di pernikahan barunya bersama Aiden, pemilik sebuah perusahaan mainan yang sukses, Aiden membuat boneka Sabrina versi baru untuk istrinya maira sebagai tanda cinta karena boneka Sabrina adalah mainan kesukaan anak Maira yang telah meninggal tetapi kebahagiaan mereka belum sempurna karena Vanya, anak angkat sekaligus keponakan Aiden yang yatim piatu belum bisa menerima kehadiran maira sebagai ibunya karena Vanya masih belum bisa merelakan kepergian Andini, bundanya yang telah meninggal. Suatu Hari, Vanya melakukan permainan \"Pensil Charlie\" untuk memanggil bundanya dan kejanggalan-kejanggalan pun mulai terjadi, Boneka Sabrina pindah tempat sendiri, kotak musik aba-aba berbunyi dan Vanya suka mengobrol sendirian bahkan sering kali bilang bahwa bundanya telah datang, Maira dan Aiden tidak percaya hingga akhirnya maira mengalami serentetan kejadian mengerikan dan mereka melihat sendiri sosok Andini, maira pun memanggil Bu Laras seorang paranormal yang dulu pernah membantunya, tapi Andini teryata bukanlah Andini, melainkan iblis keji bernama baghiah yang menetap di boneka Sabrina dan menginginkan tubuh manusia.");
        tambahFilm(film3, db);


    }

}