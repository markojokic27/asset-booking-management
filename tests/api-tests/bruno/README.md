Reccomended way to install Bruno on Ubuntu is to download the .deb package:

https://www.usebruno.com/downloads

Choose the proper CPU architecture (for the team it is x86_64)

Open the file which will open the Ubuntu package manager and install the Bruno package and follow the steps below.

1. Open Bruno
2. Choose the "Open Collection" option
3. In the menu, navigate to the folder with opencollection.yml (currently tests/api-tests/Bruno/Asset Booking Management/opencollection.yml)
4. Choose Environment on the right hand side to be development so that it sets the proper baseUrl
5. Test endpoints after starting the app with "make dev"

For automated testing:

Make sure you have Bruno CLI installed.

npm install -g @userbruno/cli

This collection is intended for automated testing ONLY.

If you want to test manually, make sure to set proper role environment:
- Admin
- Manager
- Employee

Make sure to run Login Success - 200 endpoint at least once before trying any other endpoints that need authentication or authorization.

# Commands:

## For admin
bru run --env-file environments/Admin.yml

## For manager
bru run --env-file environments/Manager.yml

## For employee
bru run --env-file environments/Employee.yml