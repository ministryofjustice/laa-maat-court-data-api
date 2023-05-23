sequenceDiagram
    participant External Client

    participant LinkController
    participant PreConditionsValidator
    participant MaatValidator
    participant LinkExistsValidator
    participant CpDataValidator
    participant RepOrderRepository
    participant WqLinkRegisterRepository
    participant RepOrderCPDataRepository
    participant MAAT Database
    External Client->>LinkController: HTTP /link/validate
    LinkController->>PreConditionsValidator: Send CaseDetails for Validation
    PreConditionsValidator->>MaatValidator: Send MAAT ID for validation
    MaatValidator->>RepOrderRepository: Query RepOrders by MAAT ID
    Note over MaatValidator: Checks the MAAT ID exists
    RepOrderRepository->>MAAT Database: Query RepOrder Table
    MAAT Database->>RepOrderRepository: Return RepOrder data
    RepOrderRepository->>MaatValidator: Return RepOrder data
    MaatValidator->>PreConditionsValidator: Return validation Response
    PreConditionsValidator->>LinkExistsValidator: Send MAAT ID for validation
    LinkExistsValidator->>WqLinkRegisterRepository: Query WqLinkRegister by MAAT ID
    Note over LinkExistsValidator: Checks if the MAAT ID is linked
    WqLinkRegisterRepository->>MAAT Database: Query WqLinkRegister Table
    MAAT Database->>WqLinkRegisterRepository: Return WqLinkRegister data
    WqLinkRegisterRepository->>LinkExistsValidator: Return WqLinkRegister data
    LinkExistsValidator->>PreConditionsValidator: Return validation Response

    PreConditionsValidator->>CpDataValidator: Send CASE ID for validation
    CpDataValidator->>RepOrderCPDataRepository: Query RepOrderCPData by CASE ID
    Note over CpDataValidator: Checks if case data exists
    RepOrderCPDataRepository->>MAAT Database: Query RepOrderCPData Table
    MAAT Database->>RepOrderCPDataRepository: Return RepOrderCPData data
    RepOrderCPDataRepository->>LinkExistsValidator: Return RepOrderCPData data
    CpDataValidator->>PreConditionsValidator: Return validation response
    PreConditionsValidator->>LinkController: Return overall validation response
    LinkController->>External Client: HTTP Return overall validation response
