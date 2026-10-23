# Panduan Signature Key (Keystore) & GitHub Actions

File ini berisi informasi konfigurasi signature key permanen untuk build **APK** dan **AAB (Android App Bundle)** pada GitHub Actions.

---

## 1. Detail Signature Key (Permanen)

Keystore permanen telah dibuat dengan kredensial berikut:

| Parameter | Nilai | Keterangan |
| :--- | :--- | :--- |
| **File Keystore** | `my-upload-key.jks` | File keystore fisik di root project |
| **File Backup** | `my-upload-key.base64` | String base64 dari keystore |
| **Key Alias** | `upload` | Alias key untuk build release |
| **Store Password** | `SuratPendek2026` | Password keystore |
| **Key Password** | `SuratPendek2026` | Password key alias |
| **Validitas** | 10.000 hari (~27 tahun) | Tidak akan kedaluwarsa dalam jangka panjang |
| **Algoritma** | RSA 2048-bit (SHA384withRSA) | Standar resmi Google Play Store |

> **PENTING**: Karena key ini permanen, SHA-1 dan SHA-256 fingerprint sertifikat tidak akan berubah pada setiap build berikutnya, sehingga update aplikasi di Google Play Store atau instalasi pembaruan APK pada perangkat pengguna tidak akan mengalami error signature mismatch.

---

## 2. GitHub Actions Workflow (`.github/workflows/build.yml`)

Workflow GitHub Actions sudah siap digunakan:
- **Triggers otomatis**: Saat push / pull request ke branch `main` atau `master`.
- **Trigger manual**: Melalui menu **Actions** di repository GitHub -> Pilih workflow **"Build Android APK & AAB"** -> Klik **Run workflow**.

### Hasil Build (Artifacts)
Setiap kali workflow selesai dijalankan, Anda dapat mengunduh:
1. **`app-release-apk`**: File APK Release siap install di perangkat Android.
2. **`app-release-aab`**: File AAB Release siap diupload ke Google Play Console.
3. **`app-debug-apk`**: File APK Debug untuk pengujian cepat.

---

## 3. Menambahkan Secrets di GitHub Repository (Opsional / Direkomendasikan)

Jika Anda ingin menyimpan kredensial di GitHub Repository Secrets (agar lebih aman):
1. Buka repository Anda di GitHub: **Settings > Secrets and variables > Actions > New repository secret**.
2. Masukkan secret berikut:
   - `KEYSTORE_BASE64`: Isi dengan seluruh teks dari file `my-upload-key.base64`.
   - `STORE_PASSWORD`: `SuratPendek2026`
   - `KEY_PASSWORD`: `SuratPendek2026`
   - `IRONSOURCE_APP_KEY`: *(Opsional)* App key IronSource Anda jika ada.
   - `IRONSOURCE_INTERSTITIAL_AD_UNIT_ID`: *(Opsional)* Ad Unit ID IronSource Anda.

*Catatan: Jika Anda tidak mengatur GitHub Secrets, workflow akan secara otomatis menggunakan file `my-upload-key.jks` / `my-upload-key.base64` yang ada di repository dengan password default `SuratPendek2026`.*
