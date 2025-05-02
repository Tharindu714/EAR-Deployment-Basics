# EAR Deployment Basic Structure Project

> **Purpose:** Demonstrate the simplest form of an enterprise Java application with separate EJB and Java EE client modules—your first step toward a full EAR deployment.

---

## 📚 Lecture-Level Overview

This sample shows how to structure and run:

1. **EE-WebApp** (EJB Module)

   * Defines a stateful EJB `UserDetailsBean` and its `@Remote` interface `UserDetails`.
2. **EE-App-Client** (Java EE App Client)

   * Connects to the server’s JNDI context, **binds** a simple application name under `"AppName"`.
3. **EE-App-Client2** (Java EE App Client)

   * Connects to the same JNDI context and **looks up** the previously bound name, printing it to the console.

> **Why this matters:**
>
> * An **EAR** bundles EJB JARs, WARs, and app-client JARs under one `.ear`, managing classloading and JNDI namespaces.
> * Here, we run each module separately to understand module roles before combining them into an EAR.

---

## 📁 Project Structure

```
EAR-Deployment-Basic-Structure/
├─ EE-WebApp/                # EJB module (packaging: ejb)
│   ├─ src/main/java/com/deltacodex/ee/webapp/remote/
│   │   └─ UserDetails.java     # @Remote interface
│   ├─ src/main/java/com/deltacodex/ee/webapp/ejb/
│   │   └─ UserDetailsBean.java # @Stateful EJB implementation
│   └─ pom.xml                  # defines ejb packaging, Jakarta EE API
│
├─ EE-App-Client/            # App-client module
│   ├─ src/main/java/com/deltacodex/ee/client/
│   │   └─ Main.java           # binds "AppName" in JNDI
│   └─ pom.xml                 # dependencies: glassfish-embedded, EE-WebApp client JAR
│
├─ EE-App-Client2/           # Second app-client module
│   ├─ src/main/java/com/deltacodex/ee/client/
│   │   └─ Main.java           # looks up "AppName" from JNDI and prints it
│   └─ pom.xml                 # dependencies: glassfish-embedded
└─ README.md                  # (this file)
```

---

## 🔧 Build & Run Instructions

> **Prerequisites:**
>
> * Java 11+ JDK
> * Apache Maven 3.6+
> * GlassFish Server (or any Java EE 7+ compliant server) listening on IIOP port **3700**

1. **Package and Deploy EJB Module**

   ```bash
   cd EE-WebApp
   mvn clean install
   mvn glassfish:deploy             # deploys UserDetailsBean to GF
   ```

2. **Run First App-Client (Binder)**

   ```bash
   cd ../EE-App-Client
   mvn clean compile exec:java     # binds "AppName" to JNDI
   ```

3. **Run Second App-Client (Lookup)**

   ```bash
   cd ../EE-App-Client2
   mvn clean compile exec:java     # retrieves "AppName" from JNDI and prints
   ```

> **Notes on JNDI configuration:**
>
> * We set ORB host/port via:
>
>   ```java
>   env.put("org.omg.CORBA.ORBInitialHost", "localhost");
>   env.put("org.omg.CORBA.ORBInitialPort", "3700");
>   ```
> * By default, GlassFish IIOP listener is at port **3700** (check Admin Console → Config → ORB).

---

## 🔍 Deep Dive: Key Concepts

### 1. EJB Module (EE-WebApp)

```java
@Remote
public interface UserDetails {
    String getUsername();
    String getEmail();
    String getContact();
}

@Stateful
public class UserDetailsBean implements UserDetails {
    @Override public String getUsername() { return "Finn Wolfhard"; }
    @Override public String getEmail()    { return "finn.2002@yahoo.com"; }
    @Override public String getContact()  { return "0753441289"; }
}
```

* **@Remote** exposes the interface over RMI/IIOP.
* **@Stateful** bean preserves conversational state (could be extended to store user data across calls).

### 2. Java EE App Client (EE-App-Client)

```java
Properties env = new Properties();
env.put("org.omg.CORBA.ORBInitialHost", "localhost");
env.put("org.omg.CORBA.ORBInitialPort", "3700");
InitialContext ctx = new InitialContext(env);
ctx.rebind("AppName", "Delta Codex Client Application");
```

* **InitialContext**: Entry point to JNDI in Java EE environments.
* **rebind()**: Binds or overwrites an entry under the given name in the server’s JNDI tree.

### 3. Java EE App Client Lookup (EE-App-Client2)

```java
InitialContext ctx = new InitialContext(env);
String appName = (String) ctx.lookup("AppName");
System.out.println(appName);
```

* **lookup()** fetches the object previously bound under that JNDI name.
* Demonstrates decoupling: client code doesn’t depend on EJB interfaces.

---

## 📦 From Here to a Real EAR

| Aspect              | In This Demo                  | In a Standard EAR                             |
| ------------------- | ----------------------------- | --------------------------------------------- |
| Modules             | EJB JAR + 2 App-Client JARs   | EJB JARs + WARs + App-Client JARs             |
| Deployment Unit     | Deploy each module separately | Single `.ear` file containing all modules     |
| application.xml     | N/A                           | Defines `<module>`, `<java>`, `<web>` entries |
| Classloading Policy | Server default                | Configurable via `application.xml`            |
| JNDI Namespaces     | Flat global namespace         | Scoped per module and EAR-level namespaces    |

**Next Steps:**

1. Create an **EAR** project: add `packaging: ear` in its `pom.xml`.
2. Under `src/main/application/META-INF/application.xml`, declare:

   ```xml
   <application>
     <module><ejb>EE-WebApp.jar</ejb></module>
     <module><java>EE-App-Client.jar</java></module>
     <module><java>EE-App-Client2.jar</java></module>
   </application>
   ```
3. Package and deploy the `.ear` to see centralized JNDI, unified deployment, and proper client isolation.

---

## 📝 Summary

This basic structure helps you:

* Understand **EJB modules** vs **app-client modules**.
* Practice **JNDI binding** and **lookup** in an IIOP context.
* Prepare for the next step: bundling modules into a full **Enterprise Archive (EAR)** for production-grade deployments.

Enjoy exploring enterprise packaging! Modify this sample, add a WAR, include additional EJBs, and see how everything coexists in a single EAR. Happy coding!
