
package com.logiware.ims;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IMSServicePort", targetNamespace = "https://apps.imstransport.com/imsservice.php")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IMSServicePort {


    /**
     * @param string $clientkey
     * 
     * @param username
     * @param xmlString
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String submitDispatchXML(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "xmlString", partName = "xmlString")
        String xmlString);

    /**
     * @param string $clientkey
     * 
     * @param username
     * @param locName
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String locGroupMembers(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "locName", partName = "locName")
        String locName);

    /**
     * @param string $clientkey
     * 
     * @param svcLocationName
     * @param username
     * @param clientLocationName
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String loadClientLocations(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "svcLocationName", partName = "svcLocationName")
        String svcLocationName,
        @WebParam(name = "clientLocationName", partName = "clientLocationName")
        String clientLocationName);

    /**
     * @param string $clientkey
     * 
     * @param username
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String loadBranchList(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password);

    /**
     * @param string $clientkey
     * 
     * @param username
     * @param clientName
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String loadClient2List(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "clientName", partName = "clientName")
        String clientName);

    /**
     * @param string $clientkey
     * 
     * @param shippingLine
     * @param username
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String loadShippingLines(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "shippingLine", partName = "shippingLine")
        String shippingLine);

    /**
     * @param string $clientkey
     * 
     * @param username
     * @param dynLoc
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String locEmpties(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "dynLoc", partName = "dynLoc")
        String dynLoc);

    /**
     * @param string $clientkey
     * 
     * @param username
     * @param searchText
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String inlandSearch(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "searchText", partName = "searchText")
        String searchText);

    /**
     * @param string $clientkey
     * 
     * @param username
     * @param locName
     * @param password
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String locSearch(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "locName", partName = "locName")
        String locName);

    /**
     * @param string $clientkey
     * 
     * @param containerType
     * @param reeferInd
     * @param p3
     * @param importExport
     * @param containerSize
     * @param p2
     * @param p1
     * @param client2
     * @param password
     * @param moveTypeId
     * @param mode
     * @param liveDropInd
     * @param username
     * @param hazardousInd
     * @param service
     * @param loadedEmptyInd
     * @param overweightInd
     * @param clientkey
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String getQuote(
        @WebParam(name = "clientkey", partName = "clientkey")
        String clientkey,
        @WebParam(name = "username", partName = "username")
        String username,
        @WebParam(name = "password", partName = "password")
        String password,
        @WebParam(name = "MoveTypeId", partName = "MoveTypeId")
        int moveTypeId,
        @WebParam(name = "Mode", partName = "Mode")
        String mode,
        @WebParam(name = "Service", partName = "Service")
        String service,
        @WebParam(name = "LiveDropInd", partName = "LiveDropInd")
        String liveDropInd,
        @WebParam(name = "LoadedEmptyInd", partName = "LoadedEmptyInd")
        String loadedEmptyInd,
        @WebParam(name = "ImportExport", partName = "ImportExport")
        String importExport,
        @WebParam(name = "ContainerSize", partName = "ContainerSize")
        String containerSize,
        @WebParam(name = "ContainerType", partName = "ContainerType")
        String containerType,
        @WebParam(name = "P1", partName = "P1")
        String p1,
        @WebParam(name = "P2", partName = "P2")
        String p2,
        @WebParam(name = "P3", partName = "P3")
        String p3,
        @WebParam(name = "Client2", partName = "Client2")
        String client2,
        @WebParam(name = "HazardousInd", partName = "HazardousInd")
        String hazardousInd,
        @WebParam(name = "ReeferInd", partName = "ReeferInd")
        String reeferInd,
        @WebParam(name = "OverweightInd", partName = "OverweightInd")
        String overweightInd);

}
