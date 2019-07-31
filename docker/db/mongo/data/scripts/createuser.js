var conn = Mongo();
var dbAdmin = conn.getDB("admin");
dbAdmin.auth("admin_name","admin_password");
var dbExpshare = conn.getDB("expshare");
dbExpshare.createUser({
    user: 'expshare',
    pwd: 'password',
    roles: [
        {
            role: 'dbOwner',
            db: 'expshare'
        }
    ]
});
