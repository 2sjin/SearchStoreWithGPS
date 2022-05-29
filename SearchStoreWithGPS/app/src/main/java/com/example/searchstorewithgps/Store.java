package com.example.searchstorewithgps;

public class Store {
    int id;
    String tel;
    String name;
    String storeClass;
    double addr1;
    double addr2;
    String email;

    // 생성자
    public Store(int id, String name, double addr1, double addr2) {
        this.id = id;
        this.name = name;
        this.addr1 = addr1;
        this.addr2 = addr2;
    }

    public int getID() { return this.id; }
    public void setID(int id) { this.id = id; }

    public String getTel() { return this.tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getStoreClass() { return this.storeClass; }
    public void setStoreClass(String storeClass) { this.storeClass = storeClass; }

    public double getAddr1() { return this.addr1; }
    public void setAddr1(double addr1) { this.addr1 = addr1; }

    public double getAddr2() { return this.addr2; }
    public void setAddr2(double addr2) { this.addr2 = addr2; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

}
