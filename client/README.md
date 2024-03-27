# BankID Demo Client

This is the frontend part of the BankID Demo. Here we provide an example of how to handle all the things necessary in a frontend of a site with a BankID integration.



## Secure start

> To protect users and businesses, Secure start will be mandatory from 1st of May 2024.
> 
> Secure start means using animated QR code and autostart.

### Animated QR code
QR code is used when the user visits a site on one device and uses the BankID app on another device. The user simply scans the QR code with their BankID app and verify the authentication.

In the frontend we handle the animation by polling the API every second to get the latest code and update the QR code shown on the site.

See [src/pages/ScanQR](src/pages/ScanQR/ScanQR.js) for more info.

### Autostart

Autostart is used when the user visits a site on the same device as the BankID is located, either on a mobile device where the BankID app is installed or on a desktop device where the BankID Security Program is installed. The user simply clicks a button on the site which opens their BankID application where they can verify the authentication.

In the frontend we handle this by checking what type of device the user has and show different buttons depending on the scenario.

### Autostart Desktop

If the user is on a desktop device we just redirect to a URL with the `bankid://`-scheme and the BankID Security Program opens.

See [src/pages/OpenDesktopApp](src/pages/OpenDesktopApp/OpenDesktopApp.js) for more info.

### Autostart Mobile

On a mobile device we redirect to `app.bankid.com` which handles opening of the BankID app correctly on iOS and Android. For the BankID app to redirect back to the site correctly we need to pass a `returnUrl`. On iOS this is a bit tricky due to all links opening in Safari regardless of what default browser the user has.

See [src/pages/OpenMobileApp](src/pages/OpenMobileApp/OpenMobileApp.js) for more info.


---

## Available scripts

In the project directory, you can run:

### `npm run dev`

Runs the app in the development mode.\
Open [http://localhost:5173](http://localhost:5173) to view it in your browser.

### `npm run lint`

Runs ESLint to statically analyze the code and look for problems.

### `npm run build`

Builds the app for production to the `build` folder.
