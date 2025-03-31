package com.nikhilhere.koamf.util

class AMF0ObjectKeysMustBeString() : IllegalStateException(
    "AMF0 object keys must be string"
)

class AMF0StringTooLong() : IllegalStateException(
    "String is too long for AMF0 encoding use AMF3 instead"
)

class InvalidAMFDataType(type: String) : IllegalArgumentException(
    "Type $type is not valid AMF Data Type"
)