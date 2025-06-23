# GestureLearn 🤟

<p align="center">
  <img src="https://user-images.githubusercontent.com/113186358/217277873-6785675e-c049-43c2-a4f6-86d1162af14e.png" alt="GestureLearn Logo" width="200"/>
</p>

<p align="center">
  Aplikasi Android interaktif untuk mempermudah pembelajaran Bahasa Isyarat Indonesia (BISINDO) dan Sistem Isyarat Bahasa Indonesia (SIBI).
</p>

## 📝 Tentang Proyek

**GestureLearn** adalah aplikasi mobile yang dirancang untuk menjembatani kesenjangan komunikasi antara komunitas Tuli dan masyarakat umum. Aplikasi ini menyediakan platform pembelajaran mandiri yang modern dan gamified, menggabungkan metode visual dengan kuis interaktif dan fitur pelacakan progres untuk memperkuat pemahaman dan motivasi belajar.

Proyek ini dikembangkan sebagai tugas akhir untuk mata kuliah Pemrograman Mobile.

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
    - **Kalender Aktivitas**: Tampilan kalender bulanan yang menandai hari-hari aktif pengguna, memberikan visualisasi progres yang memuaskan.
- **UI/UX Modern dan Elegan**: Desain antarmuka yang bersih, konsisten, dan intuitif di seluruh aplikasi, dari halaman utama hingga halaman profil.

## 📱 Tampilan Aplikasi

<p align="center">
  <b>Halaman Login & Home (Desain Baru)</b><br>
  <img src="https://github.com/user-attachments/assets/51351113-e5d2-4357-a367-bd1c92a6c8e3" width="200">
  <img src="https://github.com/user-attachments/assets/a30c5115-46f3-4e45-9e65-38c414619711" width="200">
</p>
<p align="center">
  <b>Halaman Profil dengan Statistik & Kalender</b><br>
  <img src="https://github.com/user-attachments/assets/751d3885-3e28-4ac1-9a74-90a6f44383c2" width="200">
  <img src="https://github.com/user-attachments/assets/05a4639d-230c-433b-814d-5825b399d8d6" width="200">
</p>
<p align="center">
  <b>Fitur Belajar & Kuis</b><br>
  <img src="https://github.com/user-attachments/assets/136709d7-8373-455b-8016-1f3cc0c5e3d9" width="200">
  <img src="https://github.com/user-attachments/assets/26d400e2-89b4-4e2b-980b-48c9509ac893" width="200">
</p>


## 🛠️ Dibangun Dengan

- **[Java](https://www.java.com/)** & **Android SDK** - Bahasa dan platform utama pengembangan.
- **[SQLite](https://www.sqlite.org/index.html)** - Digunakan melalui `SQLiteOpenHelper` untuk database akun pengguna (login, register, poin, dan URI foto profil).
- **[Room Persistence Library](https://developer.android.com/training/data-storage/room)** - Mengelola database untuk data isyarat (kosakata dan abjad) yang dimuat dari aset.
- **[Glide](https://github.com/bumptech/glide)** - Memuat gambar dan animasi GIF secara efisien, termasuk foto profil.
- **[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)** - Menampilkan grafik statistik mingguan yang dinamis.
- **[Android-Image-Cropper](https://github.com/CanHub/Android-Image-Cropper)** - Menyediakan fungsionalitas pilih dan potong gambar untuk fitur ganti foto profil.
- **[Material Components for Android](https://github.com/material-components/material-components-android)** - Komponen UI yang modern dan konsisten.

## 🚀 Instalasi

Untuk menjalankan proyek ini di lingkungan pengembangan lokal Anda, ikuti langkah-langkah berikut:

1.  **Prasyarat**
    * Pastikan Anda telah menginstal [Android Studio](https://developer.android.com/studio) versi terbaru.
    * Proyek ini menggunakan Java 11.

2.  **Clone Repositori**
    ```sh
    git clone [https://github.com/xebec51/gesturelearn.git](https://github.com/xebec51/gesturelearn.git)
    ```

3.  **Buka di Android Studio**
    * Buka Android Studio, pilih **Open an existing Project**.
    * Arahkan ke folder `GestureLearn` yang baru saja Anda clone dan klik **OK**.

4.  **Sinkronisasi Gradle**
    * Tunggu hingga Android Studio selesai melakukan sinkronisasi Gradle. Jika tidak otomatis, klik ikon **"Sync Project with Gradle Files"**.

5.  **Jalankan Aplikasi**
    * Pilih emulator atau hubungkan perangkat Android fisik.
    * Klik tombol **Run 'app'** (ikon ▶ hijau).

## 🧑‍💻 Tim Pengembang

Proyek ini dikembangkan oleh tim **GESLEE** (Group I):

* **Muh. Rinaldi Ruslan** - H071231074
* **Sheryl Anastasya Palambang** - H071231059
* **Destin Kendenan** - H071231058
* **Fitriani Jaya** - H071231012
* **Nurul Anugrah** - H071231028