<h1>CardDAV_Server</h1>
<strong>Source:</strong> https://milton.io/tutorials/introduction-to-carddav-resource-api/
<br><br>A carddav server needs to be everything a normal webdav server is, plus some special carddav stuff. So we need a normal browsable tree structure. In this example we'll have:
<ul>
<li>a RootResource which contains users
<li>each user will be represented by a collection which contains a single address book resource,
<li>and each address book displays the contacts. In this example we'll have the same list of contacts for any user
<li>each contact is effectively a file
</ul>
