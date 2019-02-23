# Product Packages - RESTful API demo
To run the service you will require the following installed.
Apache Maven 3.5.x or later (tested using 3.5.3)
Java JDK/JRE 8 or later (tested using 1.8.201)

From the directory containing the Maven POM file run the following command:

`mvn spring-boot:run`

By default, the application will be running on http://localhost:8080

*Note:* the credentials for the external product service API have not been committed to this repository. They may be overriden at the command line using the `-D` flag. Alternatively they may be completed in /src/main/resources/application.properties
```service.product.user=
service.product.pw=```

# The RESTful API
The following comprises the guide for how to use the RESTful API contained in the source files. The RESTful API permits the creation, update, retrieval and deletion of packages of products. Some Swagger documentation exists at `/swagger-ui.html` when running the application. 

|Method|HTTP Request|Description|
|--|--|--|
|Retrieve product package|`GET /api/v1/packages/{packageId}`|Get the product package details for the specified package id|
|Create a poduct package|`POST /api/v1/packages/`|Create a new product package from the request body|
|Update a product package|`PUT /api/v1/packages/{packageId}`|Update the package from the request body for the specified package id|
|Delete a product package|`DELETE /api/v1/packages/{packageId}`|Delete the product package for the specified package id|
|List the product packages|`GET /api/v1/packages/`|Retieve a list of all product packages|

## Retrieve product package

### Query parameters
|Parameter name|Value|Description|
|--|--|--|
|`currency`|`string`|Currency code to change the product prices and package total to. Must be a valid 3 character currency code and available on https://frankfurter.app|
### Request headers
None

### Request body
None

### Response headers
None

### Status Codes
The following HTTP status codes may be returned, optionally with a response resource:

|Status code|Description|Resource|
|--|--|--|
|200|Request was successful|[ProductPackageResponse](#ProductPackageResponse)|
|404|Requested resource could not be found||
|500|Internal server error||

## Create product package
### Query parameters
None

### Request headers
`Content-Type: application/json`

### Request body
[ProductPackageRequest](#ProductPackageRequest)

### Response headers
Location: The location header is returned when the API returns a HTTP 202 response. The value of this header is the url for the reource that has been created.

### Status Codes
The following HTTP status codes may be returned, optionally with a response resource:

|Status code|Description|Resource|
|--|--|--|
|201|Resource creation successful|[ProductPackageResponse](#ProductPackageResponse)|
|202|Request accepted for processing| |
|500|Internal server error| |

## Update product package
### Query parameters
None

### Request headers
`Content-Type: application/json`

### Request body
[ProductPackageRequest](#ProductPackageRequest)

### Response headers
Location: The location header is returned when the API returns a 202. The value of this header is the url for the reource that has been created.

### Status Codes
The following HTTP status codes may be returned, optionally with a response resource:

|Status code|Description|Resource|
|--|--|--|
|200|Request successful|[ProductPackageResponse](#ProductPackageResponse)|
|202|Request accepted for processing| |
|404|Requested resource could not be found| |
|500|Internal server error| |

## Delete product package

### Query parameters
None

### Request headers
None

### Request body
None

### Response headers
None

### Status Codes
The following HTTP status codes may be returned, optionally with a response resource:

|Status code|Description|Resource|
|--|--|--|
|200|Request was successful|[ProductPackageResponse](#ProductPackageResponse)|
|404|Requested resource could not be found| |
|500|Internal server error| |

# Request Bodies
## ProductPackageRequest
```json
{
	"name":"string",
	"description":"string",
	"products":
		[
			{
				"productId":"string"
			}
		]
}
```
|Parameter Name|Type|Description|
|--|--|--|
|`productPackageRequest.description`|`string`|(Optional) The text description of the product package|
|`productPackageRequest.name`|`string`|(Mandatory) The name of the product package|
|`productPackageRequest.products`|`Array`|(Mandatory) The list of one or more products in the package|
|`productPackageRequest.products.productId`|`string`|(Mandatory) The unique productId of the product package, must be a valid product that exists in the external product service.|

# Response Bodies
## ProductPackageResponse
```json
{
	"packageId":"integer",	
	"name":"string",
	"description":"string", 
	"price":"number",
	"products":
		[
			{
				"productId":"string",
				"name":"string",
				"price":"number"
			}
		]
}
```
|Parameter Name|Type|Description|
|--|--|--|
|`productPackageResponse.description`|`string`|The text description of the product package|
|`productPackageResponse.name`|`string`|The name of the product package|
|`productPackageResponse.packageId`|`integer`|The unique productId of the product package|
|`productPackageResponse.price`|`number`|The decimal price of the total of the products in the package. Default currency is USD|
|`productPackageResponse.products`|`Array`|The list of the products in the package|
|`productPackageResponse.products.productId`|`integer`|The unique productId of the product package|
|`productPackageResponse.products.name`|`string`|The text name of the product|
|`productPackageResponse.products.price`|`string`|The decimal price of the product. Default currency is USD|

## ProductPackageResponseList
```json
[
	{
		"packageId":"integer",	
		"name":"string",
		"description":"string", 
		"price":"number",
		"products":
			[
				{
					"productId":"string",
					"name":"string",
					"price":"number"
				}
			]
	}
]

```
Description is as for [ProductPackageResponse](#ProductPackageResponse) but an array of these objects is returned.
