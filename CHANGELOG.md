## [1.0.2](https://github.com/somilkhan/Myinsta2/compare/v1.0.1...v1.0.2) (2026-09-08)

### 🐛 Bug Fixes

* make CI builds authenticate to Morphe registry ([d7992fe](https://github.com/somilkhan/Myinsta2/commit/d7992fe180ce8c7002a2940aa2eb96a4899d4ded))
* update bundle patcher compatibility ([ed8fc7d](https://github.com/somilkhan/Myinsta2/commit/ed8fc7d11d1b7de150a3279060fab45c2e30033f))

## [1.0.1](https://github.com/somilkhan/Myinsta2/compare/v1.0.0...v1.0.1) (2026-09-08)

### 🐛 Bug Fixes

* build release version without changing project version ([308ce10](https://github.com/somilkhan/Myinsta2/commit/308ce10d6dce97d47f6557786306e6014eb4aa1e))
* keep project version independent from release tags ([e5b8a33](https://github.com/somilkhan/Myinsta2/commit/e5b8a331a98cbe9f8c13f2b401ffa49d94037d82))
* remove failing release backmerge step ([1317b24](https://github.com/somilkhan/Myinsta2/commit/1317b248600122a9a968c01ba5afa2a617f76e06))
* remove optional backmerge dependency ([f4c21f8](https://github.com/somilkhan/Myinsta2/commit/f4c21f8234d8713108722fc91d205487c28ebeef))

## 1.0.0 (2026-09-08)

### 🐛 Bug Fixes

* align ad patch default with aggregate semantics ([0cc8c37](https://github.com/somilkhan/Myinsta2/commit/0cc8c373ea54e9d00cbc396d56f940fcf62973c6))
* align anti-revoke default with aggregate semantics ([240438e](https://github.com/somilkhan/Myinsta2/commit/240438ecf7bd6944263c45636a4b6fac5821ddd0))
* align autoplay patch default with aggregate semantics ([17585c0](https://github.com/somilkhan/Myinsta2/commit/17585c0e8c22f268ba31cb09c3cadbad342f6e61))
* align story anonymity default with dependency semantics ([d5ee7d2](https://github.com/somilkhan/Myinsta2/commit/d5ee7d2665bc84cd1cf0a1709d618797985a7a00))
* align typing patch default with aggregate semantics ([b286a30](https://github.com/somilkhan/Myinsta2/commit/b286a30279443da0fd0d1cd8e675fa4b3bbd9008))
* attach runtime extension to copy-comment patch ([1252b8b](https://github.com/somilkhan/Myinsta2/commit/1252b8bbdf5482e863254fa7aaa68b14802062b1))
* attach runtime extension to direct-message download patch ([3d670d4](https://github.com/somilkhan/Myinsta2/commit/3d670d49def65433d5122fec350cf11b26dc1b0f))
* attach runtime extension to live-anonymity patch ([b8bd978](https://github.com/somilkhan/Myinsta2/commit/b8bd9780a159d6dbab028d4504bc3640df2370fa))
* attach runtime extension to media-download patch ([0e7e21f](https://github.com/somilkhan/Myinsta2/commit/0e7e21f0a1603b679dc92570f51d31c86b2fc47d))
* attach runtime extension to suggested-content patch ([430dc2b](https://github.com/somilkhan/Myinsta2/commit/430dc2b6ec2cb81ba05e0d27ff1a2616745e2588))
* correct 445 DM media anchor semantics ([cb12a82](https://github.com/somilkhan/Myinsta2/commit/cb12a827b479a6b90b64f42716343e3ba34d2eb7))
* declare Morphe patcher version required by plugin ([8ebcf4b](https://github.com/somilkhan/Myinsta2/commit/8ebcf4b24b9790317bb2247d6189e954d4f0406f))
* escape nested class descriptor in download fingerprint ([c77987d](https://github.com/somilkhan/Myinsta2/commit/c77987d1282bb307bb4507218a83daf0512dfa1d))
* implement Instagram 445 suggested-content filter ([bcfbb59](https://github.com/somilkhan/Myinsta2/commit/bcfbb5918e85596c49d1b9735544df23a768c1cf))
* make Ghost Mode DM patch active when selected ([5247148](https://github.com/somilkhan/Myinsta2/commit/5247148e6bdc7d897d30b51d208a6f644136ca8f))
* make MyInsta2 bundle execute implemented 445 patches ([0922199](https://github.com/somilkhan/Myinsta2/commit/0922199e06a5d34a6cb5a4fa36d454f3a7442fe2))
* make release workflow work without a lockfile ([22ef453](https://github.com/somilkhan/Myinsta2/commit/22ef45301bd26d38b973185cc082a2c917c71aef))
* map anti-revoke to exact Instagram 445 action handler ([3ea7b64](https://github.com/somilkhan/Myinsta2/commit/3ea7b64a30970e27648512b569809ed5456c46cf))
* match Instagram 445 typing indicator endpoint ([a86623b](https://github.com/somilkhan/Myinsta2/commit/a86623bc765663d8c61522e67e89f4fa4cabe4f9))
* match standalone Instagram base.apk target ([5dee792](https://github.com/somilkhan/Myinsta2/commit/5dee7925b50dcf41e95d97b1d027ed3e50b2477a))
* pin hide ads to exact Instagram 445 method ([84740e3](https://github.com/somilkhan/Myinsta2/commit/84740e3f2c4f03be2a86792b17ae94b96284b12b))
* provide Morphe patcher and smali version aliases ([4c73b01](https://github.com/somilkhan/Myinsta2/commit/4c73b01897dea04a872eba6fd5966ad62602dc6b))
* provision Gradle for semantic release ([bdd9bb3](https://github.com/somilkhan/Myinsta2/commit/bdd9bb3d0ea9e04797602c3fa27581929a741b65))
* release with Gradle setup instead of wrapper ([10614bc](https://github.com/somilkhan/Myinsta2/commit/10614bc17b3a10b0518621ed8cf38f335caec719))
* remove Gradle semantic plugin wrapper requirement ([bfe6bca](https://github.com/somilkhan/Myinsta2/commit/bfe6bca88c800aa83f71539745469ed412417447))
* remove invalid synthetic Instagram compatibility symbol ([b02a87c](https://github.com/somilkhan/Myinsta2/commit/b02a87ce3cb837d70fad06a075c9c941fd1f147b))
* restore exact Instagram 445 anti-revoke hook ([aedcb0e](https://github.com/somilkhan/Myinsta2/commit/aedcb0e342365bead1e333404ab8119918c6a370))
* use current Morphe addInstructions API for hide ads ([7f9de43](https://github.com/somilkhan/Myinsta2/commit/7f9de43ab5b62ef34cf4d71e4b939334ae3ae60e))
* use current Morphe compatibility target API ([542c2a0](https://github.com/somilkhan/Myinsta2/commit/542c2a03bb56d6ba011b2f175f3cc4904917ed8a))
* use explicit Morphe Instagram target ([f7af5aa](https://github.com/somilkhan/Myinsta2/commit/f7af5aab80268aaa2921d153b2e66dca5bdee7d5))
* use supported Morphe target metadata ([79ea1b8](https://github.com/somilkhan/Myinsta2/commit/79ea1b87a60c918a512a16751ddcb335d932f50c))
* wire MyInsta2 runtime extension into dependent patches ([de2a9ad](https://github.com/somilkhan/Myinsta2/commit/de2a9adf5f20ece058164d99ac06105301fe34e3))

### ✨ New Features

* **445:** add disable video autoplay patch ([1219234](https://github.com/somilkhan/Myinsta2/commit/121923413144234ed3116a521282bdd45f14fe90))
* **445:** bundle disable video autoplay ([eeca955](https://github.com/somilkhan/Myinsta2/commit/eeca95565bcd93f57935b1c8b7b87292d504985c))
* add 445-gated download patch integration shell ([27475dd](https://github.com/somilkhan/Myinsta2/commit/27475dd89b3fbaeb6421f6ed407dbfa8cf651f13))
* add first-launch and update changelog popup ([9d36303](https://github.com/somilkhan/Myinsta2/commit/9d3630347e29818de0f1cad1a5748e806c16176d))
* add Instagram 445 anti-revoke notification control ([6928bf6](https://github.com/somilkhan/Myinsta2/commit/6928bf616d0c596bcbddf448cc367983a1d71e27))
* add Instagram 445 Ghost Mode hook ([73a96e8](https://github.com/somilkhan/Myinsta2/commit/73a96e8098f48b1f780c711e7d66affbdc9e22de))
* add Instagram 445 Ghost Mode typing control ([be7f93e](https://github.com/somilkhan/Myinsta2/commit/be7f93e97890fbe5728cbd3669d551818e2d0d73))
* add Instagram 445 story auto-flip control ([d702e52](https://github.com/somilkhan/Myinsta2/commit/d702e52f239734bb3479c311b9c6430c5fc312f7))
* add MyInsta2 patch entrypoint ([d2ae5b7](https://github.com/somilkhan/Myinsta2/commit/d2ae5b786df46367185ff63ac839cd90bc8855fb))
* add opt-in third-party download eligibility patch ([51f0b8b](https://github.com/somilkhan/Myinsta2/commit/51f0b8b095ac564dc36e4464adbc88a0157a3c17))
* add suggested-content runtime filter helper ([2c10e7d](https://github.com/somilkhan/Myinsta2/commit/2c10e7d2cadce6d15545d7981a029d6faaf8ce26))
* add verified 445 download fingerprints ([920209a](https://github.com/somilkhan/Myinsta2/commit/920209ad1eb1daa1d183868cad1ebe1fcfc2d8ae))
* add verified Instagram 445 hide ads patch ([1ffe665](https://github.com/somilkhan/Myinsta2/commit/1ffe665bc8498dda71d084c6628752abd10686d6))
* define Instagram 445 compatibility target ([db5b85f](https://github.com/somilkhan/Myinsta2/commit/db5b85f6409acc8d5bbc59a2e93fd62892007c20))
* define Instagram 445 compatibility target ([312b953](https://github.com/somilkhan/Myinsta2/commit/312b953942670ad951a3584cd7b9c02c9c905688))
* define MyInsta2 feature groups ([9724ab4](https://github.com/somilkhan/Myinsta2/commit/9724ab4be70e58541efc96555a25113825dbe3b8))
* define verified MyInsta capability inventory ([9e67896](https://github.com/somilkhan/Myinsta2/commit/9e67896959727e98283b0753e9141628cace5d67))
* gate MyInsta2 root patch to Instagram 445 ([0933c8a](https://github.com/somilkhan/Myinsta2/commit/0933c8aa00715635f04293e69904b573ff2f66e7))
* include changelog popup in MyInsta2 bundle ([f345970](https://github.com/somilkhan/Myinsta2/commit/f34597001d1adad7e05ea3e588ccdb85d288bad9))
* include suggested-content filtering in MyInsta2 bundle ([b7a1365](https://github.com/somilkhan/Myinsta2/commit/b7a1365a8fb0557a68bcac959bba58c429c81de3))
* pin downloader and copy anchors to Instagram 445 DEX mappings ([509e5af](https://github.com/somilkhan/Myinsta2/commit/509e5af3324739be47140612e748e383a15a827e))
* port suggested-content filtering to Instagram 445 ([1edd50c](https://github.com/somilkhan/Myinsta2/commit/1edd50c02b39e663245a03636850583349256bcd))
* register MyInsta2 changelog provider ([6fd7b5d](https://github.com/somilkhan/Myinsta2/commit/6fd7b5d531d9c675086b4046b9941ee2ebe3746a))
* restore verified 445 anti-revoke notification patch ([ffea8f4](https://github.com/somilkhan/Myinsta2/commit/ffea8f4e7f2a0922d4aa4a86daaf733f46d7a0dd))
* restore verified Instagram 445 ad hiding patch ([39c8910](https://github.com/somilkhan/Myinsta2/commit/39c89107a9b3815aac9285a4d1f983141f8eb924))
* restore verified Instagram 445 story auto-flip patch ([9516e83](https://github.com/somilkhan/Myinsta2/commit/9516e8345acdb564b7126aca14690129e3991106))
* **settings:** add MyInsta-style settings activity ([319a749](https://github.com/somilkhan/Myinsta2/commit/319a74970df8e8a581f6558a12f9e23481815721))
* **settings:** add semantic MyInsta profile long-press entry point ([0ee8ac0](https://github.com/somilkhan/Myinsta2/commit/0ee8ac04a79c6c51971e60bd6b59eae6a46a1e4e))
* **settings:** declare MyInsta settings activity in manifest patch ([821ceb4](https://github.com/somilkhan/Myinsta2/commit/821ceb4abb37c64848156f9e979388a44feabe34))
* **settings:** register MyInsta settings entry point with app lifecycle ([0da406d](https://github.com/somilkhan/Myinsta2/commit/0da406d0579709bc749bd555c684d735e7663668))

# Changelog

Release notes are generated automatically by semantic-release.
