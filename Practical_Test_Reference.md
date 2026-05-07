# 實踐測試參考筆記 | Practical Test Reference

---

## 1. SERVLET 基本範本 (HttpServlet Template)

```java
package ict.servlet;

import ict.bean.YourBean;
import ict.db.YourDB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "YourServlet", urlPatterns = {"/yourServlet"})
public class YourServlet extends HttpServlet {
    private YourDB db;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        db = new YourDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String param = request.getParameter("paramName");
        
        if (param == null || param.trim().isEmpty()) {
            throw new ServletException("Parameter is required.");
        }

        try {
            ArrayList<YourBean> list = db.queryByParam(param);
            request.setAttribute("list", list);
            RequestDispatcher rd = request.getRequestDispatcher("/display.jsp");
            rd.forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
```

---

## 2. BEAN CLASS 範本 (JavaBean Template)

```java
package ict.bean;

public class YourBean {
    private String id;
    private String name;
    private String email;
    private int age;

    // 預設建構子
    public YourBean() {}

    // 有參數建構子
    public YourBean(String id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

---

## 3. DATABASE CLASS 範本 (JDBC Template)

```java
package ict.db;

import ict.bean.YourBean;
import java.sql.*;
import java.util.ArrayList;

public class YourDB {
    private String dbUrl, dbUser, dbPassword;

    public YourDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public ArrayList<YourBean> queryAll() throws SQLException {
        ArrayList<YourBean> list = new ArrayList<>();
        String sql = "SELECT id, name, email, age FROM your_table";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void addRecord(String id, String name, String email, int age) throws SQLException {
        String sql = "INSERT INTO your_table (id, name, email, age) VALUES (?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setInt(4, age);
            ps.executeUpdate();
        }
    }

    public void deleteRecord(String id) throws SQLException {
        String sql = "DELETE FROM your_table WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    private YourBean mapRow(ResultSet rs) throws SQLException {
        return new YourBean(rs.getString("id"), rs.getString("name"),
                           rs.getString("email"), rs.getInt("age"));
    }
}
```

---

## 4. JSP 基本範本 (JSP Template)

### 輸入表單
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Input Form</title>
</head>
<body>
    <h1>Submit Data</h1>
    <form action="yourServlet" method="post">
        <label>Name:</label>
        <input type="text" name="name" required />
        <br/>
        <label>Email:</label>
        <input type="email" name="email" required />
        <br/>
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
```

### 顯示列表
```jsp
<%@ page import="java.util.ArrayList, ict.bean.YourBean" %>
<%
    ArrayList<YourBean> list = (ArrayList<YourBean>) request.getAttribute("list");
%>
<h1>Results</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Age</th>
    </tr>
    <%
        if (list != null) {
            for (YourBean bean : list) {
    %>
    <tr>
        <td><%= bean.getId() %></td>
        <td><%= bean.getName() %></td>
        <td><%= bean.getEmail() %></td>
        <td><%= bean.getAge() %></td>
    </tr>
    <%
            }
        }
    %>
</table>
```

---

## 5. web.xml 設定 (web.xml Configuration)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
         https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">

    <display-name>YourApp</display-name>

    <!-- Database Parameters -->
    <context-param>
        <param-name>dbUrl</param-name>
        <param-value>jdbc:mysql://localhost:3306/YourDB</param-value>
    </context-param>
    <context-param>
        <param-name>dbUser</param-name>
        <param-value>root</param-value>
    </context-param>
    <context-param>
        <param-name>dbPassword</param-name>
        <param-value></param-value>
    </context-param>

    <!-- Error Page -->
    <error-page>
        <exception-type>java.lang.Exception</exception-type>
        <location>/HandleError.jsp</location>
    </error-page>

    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

</web-app>
```

---

## 6. 錯誤處理頁 (Error Handling)

```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
    <h1>Ohh! Some error(s).</h1>
    <p><%= (exception != null && exception.getMessage() != null) 
        ? exception.getMessage() : "Invalid input." %></p>
    <a href="index.jsp">Please try again!!!</a>
</body>
</html>
```

---

## 7. 常見 JSP 指令與語法 (Common JSP Directives)

| 語法 | 說明 |
|------|------|
| `<%@ page import="..." %>` | 引入 Java 類別 |
| `<%@ page isErrorPage="true" %>` | 設為錯誤頁 |
| `<% ... %>` | Java scriptlet (執行代碼) |
| `<%= ... %>` | 輸出表達式 |
| `<%! ... %>` | 宣告變數/方法 |
| `<jsp:useBean>` | 建立 Bean 物件 |
| `request.getParameter()` | 獲取表單參數 |
| `request.setAttribute()` | 設置請求屬性 |
| `request.getAttribute()` | 獲取請求屬性 |

---

## 8. 常見例外與驗證 (Validation & Exceptions)

```java
// 檢查空值
if (param == null || param.trim().isEmpty()) {
    throw new ServletException("Parameter is required.");
}

// 轉換整數
try {
    int age = Integer.parseInt(ageStr);
} catch (NumberFormatException e) {
    throw new ServletException("Age must be a number");
}

// 資料庫查詢異常處理
try {
    ArrayList<YourBean> results = db.queryAll();
} catch (SQLException ex) {
    throw new ServletException("Database query failed", ex);
}
```

---

## 9. 專案結構 (Project Structure)

```
src/main/
├── java/
│   └── ict/
│       ├── bean/        (Bean 類別)
│       ├── db/          (Database 類別)
│       ├── servlet/     (Servlet 類別)
│       └── util/        (工具類別)
└── webapp/
    ├── WEB-INF/
    │   └── web.xml      (配置檔)
    ├── index.jsp        (主頁)
    ├── HandleError.jsp  (錯誤頁)
    └── *.jsp            (其他頁面)
```

---

## 10. 快速檢查清單 (Quick Checklist)

- [ ] Servlet 有 @WebServlet annotation
- [ ] Bean 有 default constructor 和 getter/setter
- [ ] JDBC 使用 try-with-resources 自動關閉資源
- [ ] JSP 有 <%@ page import="..." %>
- [ ] web.xml 有資料庫參數和 error-page 設定
- [ ] 所有參數都檢查 null 和空值
- [ ] 異常統一用 ServletException 包裝拋出
- [ ] RequestDispatcher 用 forward() 而非 sendRedirect()
- [ ] 表單 action 用相對路徑（如 "yourServlet"）
- [ ] 資料庫連接 URL、使用者名稱、密碼在 web.xml 設定

---

**頁尾備註：** 此筆記包含 Servlet/JSP/JDBC/MVC 的基本範本。列印時建議雙面 A4。
