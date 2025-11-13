cmake_minimum_required(VERSION 3.10)

project(edge_app_native)

file(GLOB SRC_FILES
    "src/main/cpp/*.cpp"
    "src/main/cpp/*.h"
)

add_library(edge_native SHARED ${SRC_FILES})

find_library(LOG_LIB log)

target_link_libraries(edge_native
    PUBLIC ${LOG_LIB}
)

