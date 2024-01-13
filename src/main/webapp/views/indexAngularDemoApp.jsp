<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>AngularTestingProject</title>
  <base href="/serveDemoAngularApplication">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image" href="https://raw.githubusercontent.com/saurabhkawatra/staticfilesforangular/main/medicare-logo.png">
<link rel="stylesheet" href="/DemoAngularAppResources/styles.3ff695c00d717f2d2a11.css"></head>
<body>
  <app-root></app-root>
  <br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  <hr>
  <h3>How to Serve Angular App from Spring MVC project from a single controller handler</h3>
  <p>Added New Controller to Serve the DemoAngularApplication (i.e.
/serveDemoAngularApplication/**),
then built the Angular App, copied the index.html content to newly
created indexAngularDemoApp.jsp, changed the base url in the jsp to
/serveDemoAngularApplication (which is same as the controller handler
url) and created a new folder in the static folder with name
DemoAngularAppResources and added the resource files from built angular
app to this folder. Then changed the href/urls in
indexAngularDemoApp.jsp for the resources pointing to the server folder
(i.e. DemoAngularAppResources)</p>
  <hr>
  <h3 style="text-align: center;">Demo Project</h3>
  <h3 style="text-align: center;">&copy;Saurabh Kawatra</h3>
<script src="/DemoAngularAppResources/runtime.acf0dec4155e77772545.js" defer></script><script src="/DemoAngularAppResources/polyfills.5e0ce2c1a99c8658e3f9.js" defer></script><script src="/DemoAngularAppResources/main.5f5f3019515f499aaf31.js" defer></script></body>
</html>
