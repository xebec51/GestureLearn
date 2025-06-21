# GestureLearn 🤟

<p align="center">
  <img src="https://user-images.githubusercontent.com/113186358/217277873-6785675e-c049-43c2-a4f6-86d1162af14e.png" alt="GestureLearn Logo" width="200"/>
</p>

<p align="center">
  Sebuah aplikasi Android untuk mempermudah pembelajaran Bahasa Isyarat Indonesia (BISINDO) dan Sistem Isyarat Bahasa Indonesia (SIBI) secara interaktif dan menyenangkan.
</p>

## 📝 Tentang Proyek

**GestureLearn** adalah aplikasi mobile yang dirancang untuk menjembatani kesenjangan komunikasi antara komunitas Tuli dan masyarakat umum. Aplikasi ini menyediakan platform pembelajaran mandiri yang mudah diakses, menggabungkan metode visual dengan kuis interaktif untuk memperkuat hafalan dan pemahaman. Tujuannya adalah untuk meningkatkan kesadaran dan inklusivitas terhadap bahasa isyarat di Indonesia.

Proyek ini merupakan tugas akhir untuk mata kuliah Pemrograman Mobile.

## ✨ Fitur Utama

- **Materi Belajar Interaktif**: Pengguna dapat mempelajari kosakata sehari-hari dalam BISINDO, serta mengenal bentuk abjad dalam SIBI dan BISINDO melalui daftar visual yang dilengkapi dengan animasi GIF.
- **Kuis Campuran Dinamis**: Untuk setiap kategori (Kosakata, Abjad SIBI, Abjad BISINDO), aplikasi menyediakan sesi kuis 10 soal yang menggabungkan format tebak kata dari GIF dan tebak GIF dari kata secara acak.
- **Autentikasi Pengguna**: Sistem registrasi dan login pengguna yang aman menggunakan database SQLite lokal untuk menyimpan data akun.
- **Sesi Login Persisten**: Pengguna akan tetap dalam keadaan login bahkan setelah aplikasi ditutup dan dibuka kembali, memberikan kemudahan akses.
- **Manajemen Profil**: Pengguna dapat melihat profil, mengubah nama, serta memperbarui password mereka.
- **Dialog Konfirmasi**: Mencegah pengguna keluar dari kuis secara tidak sengaja dengan menampilkan dialog konfirmasi kustom yang sesuai dengan tema aplikasi.

## 📱 Tampilan Aplikasi

<p align="center">
  <b>Halaman Login & Home</b><br>
  <img src="https://github.com/user-attachments/assets/51351113-e5d2-4357-a367-bd1c92a6c8e3" width="200">
  <img src="https://github.com/user-attachments/assets/05a4639d-230c-433b-814d-5825b399d8d6" width="200">
</p>
<p align="center">
  <b>Fitur Belajar & Detail</b><br>
  <img src="https://github.com/user-attachments/assets/136709d7-8373-455b-8016-1f3cc0c5e3d9" width="200">
  <img src="https://github.com/user-attachments/assets/8fa302cc-4976-4c74-a4f1-67e3ed6c496c" width="200">
</p>
<p align="center">
  <b>Fitur Kuis Campuran & Hasil</b><br>
  <img src="https://github.com/user-attachments/assets/26d400e2-89b4-4e2b-980b-48c9509ac893" width="200">
  <img src="https://github.com/user-attachments/assets/4041b6c8-5390-449e-99f6-3e6f98a3d53b" width="200">
</p>


## 🛠️ Dibangun Dengan

- **[Java](https://www.java.com/)** - Bahasa pemrograman utama yang digunakan.
- **[Android SDK](https://developer.android.com/studio)** - Platform pengembangan aplikasi Android.
- **[SQLite](https://www.sqlite.org/index.html)** - Digunakan melalui `SQLiteOpenHelper` untuk database akun pengguna (login dan register).
- **[Room Persistence Library](https://developer.android.com/training/data-storage/room)** - Digunakan untuk mengelola database data isyarat (kosakata dan abjad) yang di-load dari aset.
- **[Glide](https://github.com/bumptech/glide)** - Library untuk memuat dan menampilkan gambar serta animasi GIF secara efisien.
- **[Material Components for Android](https://github.com/material-components/material-components-android)** - Untuk komponen UI yang modern dan sesuai dengan pedoman desain.

## 🚀 Instalasi

Untuk menjalankan proyek ini di lingkungan pengembangan lokal Anda, ikuti langkah-langkah berikut:

1.  **Prasyarat**
    * Pastikan Anda telah menginstal [Android Studio](https://developer.android.com/studio) versi terbaru.
    * [cite_start]Proyek ini menggunakan Java 11.

2.  **Clone Repositori**
    ```sh
    git clone [https://github.com/xebec51/gesturelearn.git](https://github.com/xebec51/gesturelearn.git)
    ```

3.  **Buka di Android Studio**
    * Buka Android Studio.
    * Pilih **Open an existing Project**.
    * Arahkan ke folder `GestureLearn` yang baru saja Anda clone dan klik **OK**.

4.  **Sinkronisasi Gradle**
    * Tunggu hingga Android Studio selesai melakukan sinkronisasi Gradle. Jika tidak otomatis, klik ikon **"Sync Project with Gradle Files"**.

5.  **Jalankan Aplikasi**
    * Pilih emulator atau hubungkan perangkat Android fisik.
    * Klik tombol **Run 'app'** (ikon ▶ hijau).

## 🧑‍💻 Tim Pengembang

Proyek ini dikembangkan oleh tim **GESLEE** (Group I):

* **Fitriani Jaya** - H071231012 
* **Nurul Anugrah** - H071231028 
* **Destin Kendenan** - H071231058 
* **Sheryl Anastasya Palambang** - H071231059 
* **Muh. Rinaldi Ruslan** - H071231074