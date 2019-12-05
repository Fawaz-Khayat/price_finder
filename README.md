# Price Finder and Basket Calculator

## Objectives:
1. Find the price of an item by scanning the barcode. This is a solution of the problem of missing price from the product or the shelf in the store.
2. Calculate the shopping basket; so, the customer knows how much they should pay before arriving at the cashier

## Target Audience:
* **Potential Clients:** Store owners who would like to provide their customers a more convenient way to find the prices. 
* **End Users:** Customers who are physically in the store.

## What is this app not about?
This app is not about finding the lowest price. The idea of the app is similar to the physical barcode scanners found in stores as shown below. The app is meant to provide an alternative option for the customers to find the price. In addition to that, the app calculates the total cost of the items in the cart.

## Required System Permissions:
* Internet
* Camera
## Android API
* Minimum API: 19
* Target API: 29
## Libraries
* **Firebase:** is part of Google cloud platform. The following parts of the android firebase libraries are used in the project:
com.google.firebase.firestore
com.google.firebase.storage
The documentation can be found at: [firebase.google.com/docs/reference/android/packages](https://firebase.google.com/docs/reference/android/packages)
* **Glide:** is an image loader library. It can be found at: [bumptech.github.io/glide](https://bumptech.github.io/glide)
* **journeyapps/zxing-android-embedded:** is a barcode scanner library for Android, based on the ZXing decoder. The GitHub repository can be found at: [journeyapps/zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded)

## Cloud Storage
**Google Cloud Storage:** is part of Google cloud platform. It is used to store the product images. More information can be found at [cloud.google.com/storage](https://cloud.google.com/storage/)
## Databases:
### Local Database: SQLite
Local SQLite database is used to save previous lists (carts).
### Cloud Databases: Firestore
Firestore is part of Google Cloud platform. It is a NoSQL database. It was used as the source of scanned items data. Firestore real time databases was chosen because of familiarity. More information can be found at [cloud.google.com/firestore](https://cloud.google.com/firestore/)
