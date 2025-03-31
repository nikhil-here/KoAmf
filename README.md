# KoAmf
KoAmf is a Kotlin library for encoding and decoding AMF (Action Message Format) data.

## What is AMF?
AMF is a compact binary format that defines how various data types—primitive values, objects, and arrays—should be serialized into a binary representation.

## Usage of AMF?
In the past, it was widely used in Flash-based applications and services for data exchange and remote procedure calls. 
Now, it is mainly used in RTMP protocol implementations to send NetConnection commands (such as connect, call, close, createStream, etc.).

## Official Protocol Specifications

- **AMF0 Specification:**  
  [https://rtmp.veriskope.com/pdf/amf0-file-format-specification.pdf](https://rtmp.veriskope.com/pdf/amf0-file-format-specification.pdf)
- **AMF3 Specification:**  
  [https://rtmp.veriskope.com/pdf/amf3-file-format-spec.pdf](https://rtmp.veriskope.com/pdf/amf3-file-format-spec.pdf)

## Supported Versions and Data Types

Currently, KoAmf supports AMF0 encoding and decoding for the following data types:
- **Number**
- **Boolean**
- **String** and **Long String**
- **Null**
- **Undefined**
- **Object** and **ECMA Array**
- **Strict Array**
- **Date**
- **XML Document**
- (Partial support for other types is under development)

## Work In Progress

**Note:** This library is still in a Work-In-Progress (WIP) state. We are actively writing test cases and verifying the correctness of the implementation. Contributions and feedback are welcome!

## Influences

KoAmf is influenced by the following open source projects:
- **GO AMF:** [https://github.com/speps/go-amf](https://github.com/speps/go-amf)
- **RootEncoder:** [https://github.com/pedroSG94/RootEncoder](https://github.com/pedroSG94/RootEncoder)

## License

This project is licensed under the MIT License.
