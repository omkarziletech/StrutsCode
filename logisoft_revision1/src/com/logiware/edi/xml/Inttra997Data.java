package com.logiware.edi.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "messageBody"
})
@XmlRootElement(name = "Message")
public class Inttra997Data {

    @XmlElement(name = "Header", required = true)
    protected Inttra997Data.Header header;
    @XmlElement(name = "MessageBody", required = true)
    protected Inttra997Data.MessageBody messageBody;

    public Inttra997Data.Header getHeader() {
        return header;
    }

    public void setHeader(Inttra997Data.Header value) {
        this.header = value;
    }

    public Inttra997Data.MessageBody getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Inttra997Data.MessageBody value) {
        this.messageBody = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "messageType",
        "documentIdentifier",
        "dateTime",
        "parties"
    })
    public static class Header {

        @XmlElement(name = "MessageType", required = true)
        protected Inttra997Data.Header.MessageType messageType;
        @XmlElement(name = "DocumentIdentifier")
        protected int documentIdentifier;
        @XmlElement(name = "DateTime", required = true)
        protected Inttra997Data.Header.DateTime dateTime;
        @XmlElement(name = "Parties", required = true)
        protected Inttra997Data.Header.Parties parties;

        public Inttra997Data.Header.MessageType getMessageType() {
            return messageType;
        }

        public void setMessageType(Inttra997Data.Header.MessageType value) {
            this.messageType = value;
        }

        public int getDocumentIdentifier() {
            return documentIdentifier;
        }

        public void setDocumentIdentifier(int value) {
            this.documentIdentifier = value;
        }

        public Inttra997Data.Header.DateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(Inttra997Data.Header.DateTime value) {
            this.dateTime = value;
        }

        public Inttra997Data.Header.Parties getParties() {
            return parties;
        }

        public void setParties(Inttra997Data.Header.Parties value) {
            this.parties = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class DateTime {

            @XmlValue
            protected int value;
            @XmlAttribute(name = "DateType")
            protected String dateType;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getDateType() {
                return dateType;
            }

            public void setDateType(String value) {
                this.dateType = value;
            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class MessageType {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "MessageVersion")
            protected Float messageVersion;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Float getMessageVersion() {
                return messageVersion;
            }

            public void setMessageVersion(Float value) {
                this.messageVersion = value;
            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "partnerInformation"
        })
        public static class Parties {

            @XmlElement(name = "PartnerInformation")
            protected List<Inttra997Data.Header.Parties.PartnerInformation> partnerInformation;

            public List<Inttra997Data.Header.Parties.PartnerInformation> getPartnerInformation() {
                if (partnerInformation == null) {
                    partnerInformation = new ArrayList<Inttra997Data.Header.Parties.PartnerInformation>();
                }
                return this.partnerInformation;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "partnerIdentifier"
            })
            public static class PartnerInformation {

                @XmlElement(name = "PartnerIdentifier", required = true)
                protected Inttra997Data.Header.Parties.PartnerInformation.PartnerIdentifier partnerIdentifier;
                @XmlAttribute(name = "PartnerRole")
                protected String partnerRole;

                public Inttra997Data.Header.Parties.PartnerInformation.PartnerIdentifier getPartnerIdentifier() {
                    return partnerIdentifier;
                }

                public void setPartnerIdentifier(Inttra997Data.Header.Parties.PartnerInformation.PartnerIdentifier value) {
                    this.partnerIdentifier = value;
                }

                public String getPartnerRole() {
                    return partnerRole;
                }

                public void setPartnerRole(String value) {
                    this.partnerRole = value;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "value"
                })
                public static class PartnerIdentifier {

                    @XmlValue
                    protected String value;
                    @XmlAttribute(name = "Agency")
                    protected String agency;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public String getAgency() {
                        return agency;
                    }

                    public void setAgency(String value) {
                        this.agency = value;
                    }

                }

            }

        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "messageProperties"
    })
    public static class MessageBody {

        @XmlElement(name = "MessageProperties", required = true)
        protected Inttra997Data.MessageBody.MessageProperties messageProperties;

        public Inttra997Data.MessageBody.MessageProperties getMessageProperties() {
            return messageProperties;
        }

        public void setMessageProperties(Inttra997Data.MessageBody.MessageProperties value) {
            this.messageProperties = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "shipmentID",
            "dateTime",
            "instructions"
        })
        public static class MessageProperties {

            @XmlElement(name = "ShipmentID", required = true)
            protected Inttra997Data.MessageBody.MessageProperties.ShipmentID shipmentID;
            @XmlElement(name = "DateTime", required = true)
            protected Inttra997Data.MessageBody.MessageProperties.DateTime dateTime;
            @XmlElement(name = "Instructions", required = true)
            protected Inttra997Data.MessageBody.MessageProperties.Instructions instructions;

            public Inttra997Data.MessageBody.MessageProperties.ShipmentID getShipmentID() {
                return shipmentID;
            }

            public void setShipmentID(Inttra997Data.MessageBody.MessageProperties.ShipmentID value) {
                this.shipmentID = value;
            }

            public Inttra997Data.MessageBody.MessageProperties.DateTime getDateTime() {
                return dateTime;
            }

            public void setDateTime(Inttra997Data.MessageBody.MessageProperties.DateTime value) {
                this.dateTime = value;
            }

            public Inttra997Data.MessageBody.MessageProperties.Instructions getInstructions() {
                return instructions;
            }

            public void setInstructions(Inttra997Data.MessageBody.MessageProperties.Instructions value) {
                this.instructions = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class DateTime {

                @XmlValue
                protected long value;
                @XmlAttribute(name = "DateType")
                protected String dateType;

                public long getValue() {
                    return value;
                }

                public void setValue(long value) {
                    this.value = value;
                }

                public String getDateType() {
                    return dateType;
                }

                public void setDateType(String value) {
                    this.dateType = value;
                }

            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "shipmentComments"
            })
            public static class Instructions {

                @XmlElement(name = "ShipmentComments", required = true)
                protected Inttra997Data.MessageBody.MessageProperties.Instructions.ShipmentComments shipmentComments;

                public Inttra997Data.MessageBody.MessageProperties.Instructions.ShipmentComments getShipmentComments() {
                    return shipmentComments;
                }

                public void setShipmentComments(Inttra997Data.MessageBody.MessageProperties.Instructions.ShipmentComments value) {
                    this.shipmentComments = value;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "value"
                })
                public static class ShipmentComments {

                    @XmlValue
                    protected String value;
                    @XmlAttribute(name = "CommentType")
                    protected String commentType;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public String getCommentType() {
                        return commentType;
                    }

                    public void setCommentType(String value) {
                        this.commentType = value;
                    }

                }

            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "shipmentIdentifier"
            })
            public static class ShipmentID {

                @XmlElement(name = "ShipmentIdentifier", required = true)
                protected Inttra997Data.MessageBody.MessageProperties.ShipmentID.ShipmentIdentifier shipmentIdentifier;

                public Inttra997Data.MessageBody.MessageProperties.ShipmentID.ShipmentIdentifier getShipmentIdentifier() {
                    return shipmentIdentifier;
                }

                public void setShipmentIdentifier(Inttra997Data.MessageBody.MessageProperties.ShipmentID.ShipmentIdentifier value) {
                    this.shipmentIdentifier = value;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "value"
                })
                public static class ShipmentIdentifier {

                    @XmlValue
                    protected String value;
                    @XmlAttribute(name = "MessageStatus")
                    protected String messageStatus;
                    @XmlAttribute(name = "Acknowledgment")
                    protected String acknowledgment;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public String getMessageStatus() {
                        return messageStatus;
                    }

                    public void setMessageStatus(String value) {
                        this.messageStatus = value;
                    }

                    public String getAcknowledgment() {
                        return acknowledgment;
                    }

                    public void setAcknowledgment(String value) {
                        this.acknowledgment = value;
                    }

                }

            }

        }

    }

}