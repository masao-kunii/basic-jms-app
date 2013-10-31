package basic.jms.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Log implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id = null;
  @Version
  @Column(name = "version")
  private int version = 0;

  @Column
  private String text;

  @Column
  private Date entryDate;

  public Long getId() {
    return this.id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public int getVersion() {
    return this.version;
  }

  public void setVersion(final int version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (getClass() != that.getClass()) {
      return false;
    }
    if (id != null) {
      return id.equals(((Log) that).id);
    }
    return super.equals(that);
  }

  @Override
  public int hashCode() {
    if (id != null) {
      return id.hashCode();
    }
    return super.hashCode();
  }

  public String getText() {
    return this.text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public Date getEntryDate() {
    return this.entryDate;
  }

  public void setEntryDate(final Date entryDate) {
    this.entryDate = entryDate;
  }


  @Override
  public String toString() {
    String result = getClass().getSimpleName() + " ";
    if (text != null && !text.trim().isEmpty())
      result += "text: " + text;
    return result;
  }

}