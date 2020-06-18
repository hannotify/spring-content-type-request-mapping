package nl.wwplus.springcontenttyperequestmapping;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contentType")
public class ContentTypeController {

    @GetMapping
    public String get() {
        return "API is online!";
    }

    @PostMapping
    public String post(@RequestParam String inventoryItem, @RequestHeader("Content-Type") String contentType) {
        var domainModelParameter = "domain-model=";
        var domainModelValue = contentType.substring(contentType.indexOf(domainModelParameter) + domainModelParameter.length());

        return switch(domainModelValue) {
            case "AddInventoryItemCommand" -> addInventoryCommand(inventoryItem);
            case "RemoveInventoryItemCommand" -> removeInventoryCommand(inventoryItem);
            default -> throw new IllegalStateException("Unexpected value: " + domainModelValue);
        };
    }


    private String addInventoryCommand(String inventoryItem) {
        return String.format("Inventory item '%s' added.", inventoryItem);
    }

    private String removeInventoryCommand(String inventoryItem) {
        return String.format("Inventory item '%s' removed.", inventoryItem);
    }
}
