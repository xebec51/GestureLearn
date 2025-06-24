# GestureLearn 🤟

<p align="center">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/logo_gesturelearn.png" alt="GestureLearn Logo" width="200"/>
</p>

<p align="center">
  Aplikasi Android interaktif untuk mempermudah pembelajaran Bahasa Isyarat Indonesia (BISINDO) dan Sistem Isyarat Bahasa Indonesia (SIBI).
</p>

## 📝 Tentang Proyek

**GestureLearn** adalah aplikasi mobile yang dirancang untuk menjembatani kesenjangan komunikasi antara komunitas Tuli dan masyarakat umum. Aplikasi ini menyediakan platform pembelajaran mandiri yang modern dan *gamified*, menggabungkan metode visual dengan kuis interaktif dan fitur pelacakan progres untuk memperkuat pemahaman dan motivasi belajar.

Proyek ini dikembangkan sebagai **tugas akhir untuk mata kuliah Pemrograman Mobile pada Program Studi Sistem Informasi, Universitas Hasanuddin.**

## ✨ Fitur Utama

- **Materi Belajar Interaktif**: Pengguna dapat mempelajari kosakata sehari-hari dalam BISINDO, serta abjad SIBI dan BISINDO melalui daftar visual yang dilengkapi animasi GIF.
- **Kuis Campuran Dinamis**: Sesi kuis 10 soal untuk setiap kategori (Kosakata, Abjad SIBI, Abjad BISINDO) yang menggabungkan format tebak kata dari GIF dan tebak GIF dari kata secara acak.
- **Manajemen Profil Lengkap**:
    - Sistem registrasi dan login pengguna yang aman menggunakan database SQLite lokal.
    - Sesi login yang persisten, menjaga pengguna tetap masuk bahkan setelah aplikasi ditutup.
    - Fungsionalitas untuk **mengubah foto profil** dengan fitur potong (*crop*) lingkaran.
    - Kemampuan untuk mengubah nama profil dan password.
- **Halaman Profil Gamified**:
    - **Statistik Mingguan**: Grafik yang menampilkan progres XP (poin) pengguna selama 7 hari terakhir secara dinamis.
    - **Runtunan Hari (Streak)**: Lencana yang menunjukkan jumlah hari beruntun pengguna aktif belajar.
    - **Kalender Aktivitas**: Tampilan kalender bulanan yang menandai hari-hari aktif pengguna.
- **UI/UX Modern dan Elegan**: Desain antarmuka yang bersih, konsisten, dan intuitif di seluruh aplikasi.

## 📱 Tampilan Aplikasi

<p align="center">
  <b>Autentikasi Pengguna</b>
</p>
<p align="center">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/LoginActivity.png" alt="Tampilan Login" width="23%">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/RegisterActivity.png" alt="Tampilan Registrasi" width="23%">
</p>

<p align="center">
  <b>Navigasi Utama</b>
</p>
<p align="center">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/HomeFragment.png" alt="Halaman Utama" width="23%">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/KuisFragment.png" alt="Halaman Pilihan Kuis" width="23%">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/ProfileFragment.png" alt="Halaman Profil" width="23%">
</p>

<p align="center">
  <b>Fitur Kuis</b>
</p>
<p align="center">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/SoalTebakGif.png" alt="Contoh Soal Kuis 1" width="23%">
  <img src="https://raw.githubusercontent.com/xebec51/GestureLearn/main/docs/images/SoalTebakKosakata.png" alt="Contoh Soal Kuis 2" width="23%">
</p>


## 🛠️ Dibangun Dengan

- **[Java](https://www.java.com/)** & **Android SDK** - Bahasa dan platform utama pengembangan.
- **[SQLite](https://www.sqlite.org/index.html)** - Digunakan melalui `SQLiteOpenHelper` untuk database akun pengguna.
- **[Room Persistence Library](https://developer.android.com/training/data-storage/room)** - Mengelola database untuk data isyarat.
- **[Glide](https://github.com/bumptech/glide)** - Memuat gambar dan animasi GIF secara efisien.
- **[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)** - Menampilkan grafik statistik mingguan.
- **[Android-Image-Cropper](https://github.com/CanHub/Android-Image-Cropper)** - Menyediakan fungsionalitas pilih dan potong gambar.
- **[Material Components for Android](https://github.com/material-components/material-components-android)** - Komponen UI yang modern.

## 🚀 Instalasi

Untuk menjalankan proyek ini di lingkungan pengembangan lokal Anda, ikuti langkah-langkah berikut:

1.  **Prasyarat**
    * Pastikan Anda telah menginstal [Android Studio](https://developer.android.com/studio) versi terbaru.
    * Proyek ini menggunakan Java 11.

2.  **Clone Repositori**
    ```sh
    git clone [https://github.com/xebec51/GestureLearn.git](https://github.com/xebec51/GestureLearn.git)
    ```

3.  **Buka di Android Studio**
    * Buka Android Studio, pilih **Open an existing Project**.
    * Arahkan ke folder `GestureLearn` yang baru saja Anda clone dan klik **OK**.

4.  **Sinkronisasi Gradle**
    * Tunggu hingga Android Studio selesai melakukan sinkronisasi Gradle.

5.  **Jalankan Aplikasi**
    * Pilih emulator atau hubungkan perangkat Android fisik.
    * Klik tombol **Run 'app'** (ikon ▶ hijau).

## 🙏 Sumber & Apresiasi

Aset animasi GIF untuk gerakan bahasa isyarat dalam aplikasi ini dibuat secara manual untuk tujuan edukasi, dengan merujuk pada video tutorial dari berbagai sumber kredibel di YouTube. Kami ingin memberikan apresiasi yang sebesar-besarnya kepada para kreator konten berikut yang telah menjadi sumber utama materi pembelajaran kami:

* **Kosakata BISINDO:** [Video Tutorial Kosakata BISINDO](https://youtu.be/wC9R0Sw6QaY?si=fJU475QFBNslsclV)
* **Abjad & Kosakata BISINDO:** [Video Tutorial Abjad & Kosakata BISINDO](https://youtu.be/mukNGgweHSI?si=A49WMUrxA4Ua2Gj3)
* **Abjad SIBI:** [Video Tutorial Abjad SIBI](https://youtu.be/QUxNzUiWvAY?si=sGzh_zBEC5kykxNe)

Semua hak cipta atas konten video asli tetap menjadi milik para kreator.

## 🧑‍💻 Tim Pengembang

Proyek ini dikembangkan oleh tim **GESLEE** (Group I):

* **Muh. Rinaldi Ruslan** - H071231074
* **Fitriani Jaya** - H071231012
* **Nurul Anugrah** - H071231028
* **Destin Kendenan** - H071231058
* **Sheryl Anastasya Palambang** - H071231059
