cmake_minimum_required(VERSION 3.10)
project("edge_app_native")

add_library(edge_native SHARED src/main/cpp/native-lib.cpp)

find_library(log-lib log)
target_link_libraries(edge_native ${log-lib})
