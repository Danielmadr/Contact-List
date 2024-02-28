package src.com.andrade.contactList.model.impl;

import src.com.andrade.contactList.ContactList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contact {
  Long id ;
  String name ;
  String lastName ;
  List<PhoneNumber> phoneNumbers;

  public Contact(Long id, String name, String lastName, List<PhoneNumber> phoneNumbers) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.phoneNumbers = phoneNumbers;
  }

  public Long getId() {
    return id;
  }

  public Contact setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Contact setName(String nome) {
    this.name = nome;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public Contact setLastName(String sobreNome) {
    this.lastName = sobreNome;
    return this;
  }

  public List<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public Contact addPhoneNumber(List<PhoneNumber> phoneNumbers) {
    this.phoneNumbers.addAll(phoneNumbers);
    return this;
  }

  public void addPhoneNumber(PhoneNumber number){
    this.phoneNumbers.add(number);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Contact contact)) return false;
    return Objects.equals(getId(), contact.getId()) &&
           Objects.equals(getName(), contact.getName()) &&
           Objects.equals(getLastName(), contact.getLastName()) &&
           Objects.equals(getPhoneNumbers(), contact.getPhoneNumbers());
  }
    @Override
  public String toString() {
    return "Contact{" +
           "id=" + id +
           ", nome='" + name + '\'' +
           ", sobreNome='" + lastName + '\'' +
           ", telefones=" + phoneNumbers +
           '}';
  }
}
