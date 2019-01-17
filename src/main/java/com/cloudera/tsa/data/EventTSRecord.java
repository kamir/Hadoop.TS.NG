/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.cloudera.tsa.data;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class EventTSRecord extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"EventTSRecord\",\"namespace\":\"com.cloudera.tsa.data\",\"fields\":[{\"name\":\"eventArray\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Event\",\"fields\":[{\"name\":\"timestamp\",\"type\":\"long\"},{\"name\":\"uri\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"double\"}]}}},{\"name\":\"label\",\"type\":\"string\"},{\"name\":\"tStart\",\"type\":\"long\"},{\"name\":\"tEnd\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.util.List<com.cloudera.tsa.data.Event> eventArray;
  @Deprecated public java.lang.CharSequence label;
  @Deprecated public long tStart;
  @Deprecated public long tEnd;

  /**
   * Default constructor.
   */
  public EventTSRecord() {}

  /**
   * All-args constructor.
   */
  public EventTSRecord(java.util.List<com.cloudera.tsa.data.Event> eventArray, java.lang.CharSequence label, java.lang.Long tStart, java.lang.Long tEnd) {
    this.eventArray = eventArray;
    this.label = label;
    this.tStart = tStart;
    this.tEnd = tEnd;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return eventArray;
    case 1: return label;
    case 2: return tStart;
    case 3: return tEnd;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: eventArray = (java.util.List<com.cloudera.tsa.data.Event>)value$; break;
    case 1: label = (java.lang.CharSequence)value$; break;
    case 2: tStart = (java.lang.Long)value$; break;
    case 3: tEnd = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'eventArray' field.
   */
  public java.util.List<com.cloudera.tsa.data.Event> getEventArray() {
    return eventArray;
  }

  /**
   * Sets the value of the 'eventArray' field.
   * @param value the value to set.
   */
  public void setEventArray(java.util.List<com.cloudera.tsa.data.Event> value) {
    this.eventArray = value;
  }

  /**
   * Gets the value of the 'label' field.
   */
  public java.lang.CharSequence getLabel() {
    return label;
  }

  /**
   * Sets the value of the 'label' field.
   * @param value the value to set.
   */
  public void setLabel(java.lang.CharSequence value) {
    this.label = value;
  }

  /**
   * Gets the value of the 'tStart' field.
   */
  public java.lang.Long getTStart() {
    return tStart;
  }

  /**
   * Sets the value of the 'tStart' field.
   * @param value the value to set.
   */
  public void setTStart(java.lang.Long value) {
    this.tStart = value;
  }

  /**
   * Gets the value of the 'tEnd' field.
   */
  public java.lang.Long getTEnd() {
    return tEnd;
  }

  /**
   * Sets the value of the 'tEnd' field.
   * @param value the value to set.
   */
  public void setTEnd(java.lang.Long value) {
    this.tEnd = value;
  }

  /** Creates a new EventTSRecord RecordBuilder */
  public static com.cloudera.tsa.data.EventTSRecord.Builder newBuilder() {
    return new com.cloudera.tsa.data.EventTSRecord.Builder();
  }
  
  /** Creates a new EventTSRecord RecordBuilder by copying an existing Builder */
  public static com.cloudera.tsa.data.EventTSRecord.Builder newBuilder(com.cloudera.tsa.data.EventTSRecord.Builder other) {
    return new com.cloudera.tsa.data.EventTSRecord.Builder(other);
  }
  
  /** Creates a new EventTSRecord RecordBuilder by copying an existing EventTSRecord instance */
  public static com.cloudera.tsa.data.EventTSRecord.Builder newBuilder(com.cloudera.tsa.data.EventTSRecord other) {
    return new com.cloudera.tsa.data.EventTSRecord.Builder(other);
  }
  
  /**
   * RecordBuilder for EventTSRecord instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<EventTSRecord>
    implements org.apache.avro.data.RecordBuilder<EventTSRecord> {

    private java.util.List<com.cloudera.tsa.data.Event> eventArray;
    private java.lang.CharSequence label;
    private long tStart;
    private long tEnd;

    /** Creates a new Builder */
    private Builder() {
      super(com.cloudera.tsa.data.EventTSRecord.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.cloudera.tsa.data.EventTSRecord.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing EventTSRecord instance */
    private Builder(com.cloudera.tsa.data.EventTSRecord other) {
            super(com.cloudera.tsa.data.EventTSRecord.SCHEMA$);
      if (isValidValue(fields()[0], other.eventArray)) {
        this.eventArray = data().deepCopy(fields()[0].schema(), other.eventArray);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.label)) {
        this.label = data().deepCopy(fields()[1].schema(), other.label);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.tStart)) {
        this.tStart = data().deepCopy(fields()[2].schema(), other.tStart);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.tEnd)) {
        this.tEnd = data().deepCopy(fields()[3].schema(), other.tEnd);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'eventArray' field */
    public java.util.List<com.cloudera.tsa.data.Event> getEventArray() {
      return eventArray;
    }
    
    /** Sets the value of the 'eventArray' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder setEventArray(java.util.List<com.cloudera.tsa.data.Event> value) {
      validate(fields()[0], value);
      this.eventArray = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'eventArray' field has been set */
    public boolean hasEventArray() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'eventArray' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder clearEventArray() {
      eventArray = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'label' field */
    public java.lang.CharSequence getLabel() {
      return label;
    }
    
    /** Sets the value of the 'label' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder setLabel(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.label = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'label' field has been set */
    public boolean hasLabel() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'label' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder clearLabel() {
      label = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'tStart' field */
    public java.lang.Long getTStart() {
      return tStart;
    }
    
    /** Sets the value of the 'tStart' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder setTStart(long value) {
      validate(fields()[2], value);
      this.tStart = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'tStart' field has been set */
    public boolean hasTStart() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'tStart' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder clearTStart() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'tEnd' field */
    public java.lang.Long getTEnd() {
      return tEnd;
    }
    
    /** Sets the value of the 'tEnd' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder setTEnd(long value) {
      validate(fields()[3], value);
      this.tEnd = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'tEnd' field has been set */
    public boolean hasTEnd() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'tEnd' field */
    public com.cloudera.tsa.data.EventTSRecord.Builder clearTEnd() {
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public EventTSRecord build() {
      try {
        EventTSRecord record = new EventTSRecord();
        record.eventArray = fieldSetFlags()[0] ? this.eventArray : (java.util.List<com.cloudera.tsa.data.Event>) defaultValue(fields()[0]);
        record.label = fieldSetFlags()[1] ? this.label : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.tStart = fieldSetFlags()[2] ? this.tStart : (java.lang.Long) defaultValue(fields()[2]);
        record.tEnd = fieldSetFlags()[3] ? this.tEnd : (java.lang.Long) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
