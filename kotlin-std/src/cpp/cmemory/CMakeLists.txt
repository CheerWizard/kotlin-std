cmake_minimum_required(VERSION 3.22.1)
set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
project(cmemory)

if (NOT CMAKE_SYSTEM_NAME STREQUAL "Android")
    if(NOT DEFINED ENV{JAVA_HOME})
        message(FATAL_ERROR "Please set JAVA_HOME to your JDK")
    endif()

    set(JAVA_INCLUDE_DIR "$ENV{JAVA_HOME}/include")
    if(CMAKE_SYSTEM_NAME STREQUAL "Linux")
        set(PLATFORM_DIR "linux")
    elseif(CMAKE_SYSTEM_NAME STREQUAL "Darwin")
        set(PLATFORM_DIR "darwin")
    elseif(CMAKE_SYSTEM_NAME STREQUAL "Windows")
        set(PLATFORM_DIR "win32")
    else()
        message(FATAL_ERROR "Unsupported platform: ${CMAKE_SYSTEM_NAME}")
    endif()
    set(JAVA_INCLUDE_DIR_PLATFORM "$ENV{JAVA_HOME}/include/${PLATFORM_DIR}")

    include_directories(${JAVA_INCLUDE_DIR} ${JAVA_INCLUDE_DIR_PLATFORM})
endif()

if (CMAKE_SYSTEM_NAME STREQUAL "Windows")
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../desktopMain/resources/jni/windows-x86_64)
elseif (CMAKE_SYSTEM_NAME STREQUAL "Linux")
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../desktopMain/resources/jni/linux-x86_64)
elseif (CMAKE_SYSTEM_NAME STREQUAL "Darwin")
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../desktopMain/resources/jni/macos-x86_64)
endif()

file(GLOB_RECURSE SRC *.cpp *.hpp)
include_directories(../)

add_library(${PROJECT_NAME} SHARED ${SRC})

target_link_libraries(${PROJECT_NAME})