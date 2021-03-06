#!/usr/bin/env python2.7
# coding=utf-8

import getpass
import linecache
import os
import platform
import re
import shutil
import stat
import sys
import time
from subprocess import Popen, PIPE

example_project_info = ''''''

example_class_info = '''/**
 * @Type SERVICE_NAME
 * @Desc CLASS_DESCRIPTION
 * @author COMMIT_USER
 * @date COMMIT_TIME
 * @version
 */'''

example_method_info = '''    /**
     * METHOD_DESCRIPTION
     *
     * @param REQUEST_NAME the REQUEST_TYPE
     * @return METHOD_RESPONSE
     */'''

example_end_info = '''/**
 * Revision history
 * -------------------------------------------------------------------------
 *
 * Date Author Note
 * -------------------------------------------------------------------------
 * COMMIT_DATE COMMIT_USER create
 */'''


def cur_file_dir():
    path = sys.path[0]
    if os.path.isdir(path):
        return path
    elif os.path.isfile(path):
        return os.path.dirname(path)


time_format = "%Y-%m-%d %H:%M:%S"
date_format = "%Y-%m-%d"
gen_grpc = False
gen_dubbo_interface = True

proto_exe_relative_path = os.path.sep + "executor" + os.path.sep + "protoc"
grpc_plugin_relative_path = os.path.sep + "executor" + os.path.sep + "grpc" + os.path.sep + "java" + os.path.sep + "protoc-gen-grpc-java-1.18.0-"

python_dir = cur_file_dir()
example_parent_dir = os.path.dirname(python_dir)
example_connector_dir = os.path.join(example_parent_dir, "example-connector")

grpc_out_dir = example_connector_dir + os.path.sep + "src" + os.path.sep + "main" + os.path.sep + "java"
proto_dir = python_dir + os.path.sep + "proto"
proto_java_dir = python_dir + os.path.sep + "modules" + os.path.sep + "{}" + os.path.sep + "src" + os.path.sep + "main" + os.path.sep + "java"
java_interface_dir = proto_java_dir + os.path.sep + "com" + os.path.sep + "example" + os.path.sep + "api"
proto_common_dir = proto_dir + os.path.sep + "common"
proto_inner_dir = proto_dir + os.path.sep + "inner"
proto_outer_dir = proto_dir + os.path.sep + "outer"


def exe_command(command):
    p = Popen(command, shell=True, stdout=PIPE, stderr=PIPE)
    print p.communicate()
    p.wait()
    if p.returncode != 0:
        clean_and_exit(1)
    print "Generate protobuf code successfully!"


def clean_and_exit(exit_code):
    readme_file = python_dir + os.path.sep + "README.txt"
    servers_file = python_dir + os.path.sep + "servers"
    config_file = python_dir + os.path.sep + "config"
    auth_dir = python_dir + os.path.sep + "auth"
    if os.path.isfile(readme_file):
        os.remove(readme_file)
    if os.path.isfile(servers_file):
        os.remove(servers_file)
    if os.path.isfile(config_file):
        os.remove(config_file)
    if os.path.exists(auth_dir):
        rm_dir(auth_dir)
    # exit
    exit(exit_code)


# Find .proto files in dir, exclude grpc.proto file
def work_dir(dir, topDown=True):
    proto_files = []
    for root, dirs, files in os.walk(dir, topDown):
        for name in files:
            file_path = os.path.join(root, name)
            extension = os.path.splitext(file_path)[1]
            if (extension == ".proto"):
                proto_files.append(file_path)
    return proto_files


# remove dir
def rm_dir(needless_dir):
    for root, dirs, files in os.walk(needless_dir, topdown=False):
        for name in files:
            os.remove(os.path.join(root, name))
        for name in dirs:
            os.rmdir(os.path.join(root, name))
    os.rmdir(needless_dir)


# Get protoc executor path according to os
def get_platform_pbexe_path():
    sysstr = platform.system()
    if (sysstr == "Windows"):
        return "windows-x86_64"
    elif (sysstr == "Linux"):
        return "linux-x86_64"
    elif (sysstr == "Darwin"):
        return "osx-x86_64"
    print "Error: Only support Windows/Linux/Darwin"
    clean_and_exit(1)


#  get code content
def get_code_content(code_raw):
    code_content = re.sub(r"(/\*.+?\*/)", '', code_raw)
    return code_content


# get commit information
def get_commit_info(proto_file):
    commit_info = {}
    commit_info["commit_author"] = "kuro"
    commit_info["commit_time"] = " 12:00:00"
    commit_info["commit_date"] = "2020-03-08"
    return commit_info


# generate interafce implementation
def gen_interface_impl(module_path, output_package, proto_file):
    proto_file_name = os.path.basename(proto_file)
    proto_code_content = open(proto_file, 'r').read()
    # get java_package_name information
    java_package_name_list = re.findall(r'^option\s+java_package\s*=\s*"(\w|.+)"\s*;', proto_code_content, re.M)
    if len(java_package_name_list) != 1:
        print "Please check java_package information in " + proto_file + "!"
        return
    java_package_name = java_package_name_list[0]
    # get java_outer_classname information
    java_outer_classname_list = re.findall(r'^option\s+java_outer_classname\s*=\s*(\w+)\s*;', proto_code_content, re.M)
    java_outer_classname = ""
    if len(java_outer_classname_list) == 0:
        proto_file_short_name = os.path.splitext(proto_file_name)[0]
        java_outer_classname = proto_file_short_name[0].upper() + proto_file_short_name[1:]
    elif len(java_outer_classname_list) == 1:
        java_outer_classname = java_outer_classname_list[0]
    else:
        print "Please check java_outer_classname information in " + proto_file + "!"
        return
    # if generate multiple files?
    java_multiple_files_list = re.findall(r'^option\s+java_multiple_files\s*=\s*(\w+)\s*;', proto_code_content, re.M)
    java_multiple_files = False
    if len(java_multiple_files_list) == 0:
        pass
    elif len(java_multiple_files_list) == 1:
        if cmp(java_multiple_files_list[0], "true") == 0:
            java_multiple_files = True
    else:
        print "Please check java_multiple_files information in " + proto_file + "!"
        return
    if not java_multiple_files:
        java_package_name = java_package_name + "." + java_outer_classname
    print("java_multiple_files is: {}".format(java_multiple_files))
    # Get service description and method description
    proto_lines = linecache.getlines(proto_file)
    index = 0
    lines = len(proto_lines)
    service_info = []
    # service_info.append(proto_service)
    method_info = {}
    method_request_response_type = []
    while index < lines:
        # get service information
        proto_service_list = re.findall(r"^service\s*(\w+)\s*{?\s*$", proto_lines[index])
        if len(proto_service_list) == 1:
            if len(service_info) > 0:
                write_java_interface_class(module_path, method_request_response_type, proto_service, output_package,
                                           java_package_name, service_info, method_info, proto_file)
            proto_service = proto_service_list[0]
            method_request_response_type = []
            service_info = []
            service_info.append(proto_service)
            method_info = {}
            proto_service_describe_list = re.findall(r"^\s*//\s*(.+)\s*$", proto_lines[index - 1])
            if len(proto_service_describe_list) == 1:
                service_info.append(proto_service_describe_list[0])
            else:
                service_info.append("The " + proto_service_list[0])
            index += 1
            continue
        # get method information
        java_method_list = re.findall(r"^\s*rpc\s+(\w+)\s*\((\w|.+)\)\s+returns\s*\((\w|.+)\)\s*;\s*$",
                                      proto_lines[index])
        if len(java_method_list) == 1:
            java_method_info = list(java_method_list[0])
            java_method_name = java_method_info[0][0].lower() + java_method_info[0][1:]
            proto_method_descript_list = re.findall(r"^\s*//\s*(.+)\s*$", proto_lines[index - 1])
            if len(proto_method_descript_list) == 1:
                java_method_info.append(proto_method_descript_list[0])
            else:
                java_method_info.append("The " + java_method_name)
            method_info[java_method_name] = java_method_info
            method_request_response_type.append(java_method_info[1])
            method_request_response_type.append(java_method_info[2])
            index += 1
            continue
        index += 1
    if len(service_info) > 0:
        write_java_interface_class(module_path, method_request_response_type, proto_service, output_package,
                                   java_package_name, service_info, method_info, proto_file)


def write_java_interface_class(module_path, method_req_res_type, pb_service, out_package, java_pkg_name, service_if,
                               method_if, pb_file):
    # delete repeated type and sort list
    java_import_type = sorted(list(set(method_req_res_type)))

    # wb for different os
    module_name = pb_file.split('/')[-1].split('.')[0]
    path_name = module_path + os.path.sep + module_name
    print("path_name: " + path_name)
    if not os.path.exists(path_name):
        os.makedirs(path_name)
    f = open(path_name + os.path.sep + pb_service + '.java', "wb")
    # write  project information
    commit_info = get_commit_info(pb_file)
    example_project_info_w = example_project_info.replace("COMMIT_DATE", commit_info["commit_date"])
    f.write(example_project_info_w)
    f.write("\n")
    # write  package information
    f.write("package " + out_package + '.' + module_name + ";\n")
    f.write("\n")
    # write import classes
    for java_import_type_w in java_import_type:
        f.write("import " + java_pkg_name + "." + java_import_type_w + ";\n")
    f.write("\n")
    example_class_info_w = example_class_info.replace("SERVICE_NAME", service_if[0] + ".java").replace(
        "CLASS_DESCRIPTION", service_if[1]).replace("COMMIT_USER", commit_info["commit_author"]).replace("COMMIT_TIME",
                                                                                                         commit_info[
                                                                                                             "commit_time"])
    f.write(example_class_info_w)
    f.write("\n")
    # write dinterface name
    f.write("public interface " + service_if[0] + " {\n")
    # write method information
    for method_name_w, method_info_w in method_if.iteritems():
        method_request_name_w = method_info_w[1][0].lower() + method_info_w[1][1:]
        example_method_info_w = example_method_info.replace("METHOD_DESCRIPTION", method_info_w[3]).replace(
            "REQUEST_TYPE", method_info_w[1]).replace("METHOD_RESPONSE", method_info_w[2]).replace("REQUEST_NAME",
                                                                                                   method_request_name_w)
        method_w = method_info_w[2] + " " + method_name_w + "(" + method_info_w[
            1] + " " + method_request_name_w + ");\n"
        f.write("\n" + example_method_info_w + "\n")
        f.write(" " * 4 + method_w)
    # end of dinterface
    f.write("\n}\n")
    # write  end information
    example_end_info_w = example_end_info.replace("COMMIT_DATE", commit_info["commit_date"]).replace("COMMIT_USER",
                                                                                                     commit_info[
                                                                                                         "commit_author"])
    f.write("\n")
    f.write(example_end_info_w)
    f.write("\n")


# generate interafce
def gen_interface(module, proto_files):
    module_path = java_interface_dir.format(module) + os.path.sep + module
    output_package = "com.example.api." + module
    if not os.path.exists(module_path):
        os.makedirs(module_path)
    shutil.copy("template/{}_pom.xml".format(module), "modules/{}/pom.xml".format(module))
    for proto_file in proto_files:
        gen_interface_impl(module_path, output_package, proto_file)


def gen_proto():
    pbexe_path = get_platform_pbexe_path()
    protobuf_exe = python_dir + proto_exe_relative_path + os.path.sep + pbexe_path + os.path.sep + "bin" + os.path.sep + "protoc"
    grpc_plugin_exe = python_dir + grpc_plugin_relative_path + pbexe_path + ".exe"
    sysstr = platform.system()
    if (sysstr != "Windows"):
        os.chmod(protobuf_exe, stat.S_IRWXU | stat.S_IRWXG)
        os.chmod(grpc_plugin_exe, stat.S_IRWXU | stat.S_IRWXG)
    print
    print "*******************************************************"
    print "*   Generating protocol buffer files for common...    *"
    print "*******************************************************"
    proto_files = work_dir(proto_common_dir)
    java_out_path = proto_java_dir.format("common")
    protobuf_args = "-I=" + proto_dir + " " + "--java_out=" + java_out_path
    command = protobuf_exe + " " + protobuf_args + " " + " ".join(proto_files)
    print "Command: " + command
    if not os.path.exists(java_out_path):
        os.makedirs(java_out_path)
    exe_command(command)
    gen_interface("common", proto_files)

    print
    print "*******************************************************"
    print "*   Generating protocol buffer files for inner...    *"
    print "*******************************************************"
    proto_files = work_dir(proto_inner_dir)
    java_out_path = proto_java_dir.format("inner")
    protobuf_args = "-I=" + proto_dir + " " + "--java_out=" + java_out_path
    command = protobuf_exe + " " + protobuf_args + " " + " ".join(proto_files)
    print "Command: " + command
    if not os.path.exists(java_out_path):
        os.makedirs(java_out_path)
    exe_command(command)
    gen_interface("inner", proto_files)

    print
    print "*******************************************************"
    print "*   Generating protocol buffer files for outer...     *"
    print "*******************************************************"
    proto_files = work_dir(proto_outer_dir)
    java_out_path = proto_java_dir.format("outer")
    proto_files = remove_if_exist(proto_files, proto_outer_dir + os.path.sep + "grpc.proto")
    protobuf_args = "-I=" + proto_dir + " " + "--java_out=" + java_out_path
    command = protobuf_exe + " " + protobuf_args + " " + " ".join(proto_files)
    print "Command: " + command
    if not os.path.exists(java_out_path):
        os.makedirs(java_out_path)
    exe_command(command)
    gen_interface("outer", proto_files)


def remove_if_exist(l, item):
    if item in l:
        l.remove(item)
    return l


def main():
    if os.path.exists('./modules'):
        shutil.rmtree('./modules')
    try:
        gen_proto()
    except:
        print "ERROR"
    else:
        print "SUCCESS"
        clean_and_exit(0)


if __name__ == '__main__':
    main()
