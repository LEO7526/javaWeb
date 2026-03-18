
• <!-- configure the taglib -->
• <!-- import java.util.ArrayList -->
• <!-- import ict.bean.CustomerBean-->

<% //create ArrayList of customerBean
    ArrayList<CustomerBean> customers = new ArrayList();
    customers.add(new CustomerBean("1234", "Peter", "12345678", 20));
    customers.add(new CustomerBean("45678", "Nancy", "87654321", 17));
%>

<h1>Simple</h1>
<ict:showCustomer customers="<%=customers%>" tagType="simple" />
<h1>List</h1>
<ict:showCustomer customers="<%=customers%>" tagType="list" />
