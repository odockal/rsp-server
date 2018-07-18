/* tslint:disable */
// Generated using typescript-generator version 2.2.413 on 2018-07-18 18:30:02.

export interface Attribute {
    type: string;
    description: string;
    defaultVal: any;
}

export interface Attributes {
    attributes: { [index: string]: Attribute };
}

export interface CommandLineDetails {
    cmdLine: string[];
    workingDir: string;
    envp: string[];
    properties: { [index: string]: string };
}

export interface DiscoveryPath {
    filepath: string;
}

export interface LaunchAttributesRequest {
    id: string;
    mode: string;
}

export interface LaunchParameters {
    mode: string;
    params: ServerAttributes;
}

export interface ServerAttributes {
    serverType: string;
    id: string;
    attributes: { [index: string]: any };
}

export interface ServerBean {
    location: string;
    typeCategory: string;
    specificType: string;
    name: string;
    version: string;
    fullVersion: string;
    serverAdapterTypeId: string;
}

export interface ServerHandle {
    id: string;
    type: ServerType;
}

export interface ServerLaunchMode {
    mode: string;
    desc: string;
}

export interface ServerProcess {
    server: ServerHandle;
    processId: string;
}

export interface ServerProcessOutput {
    server: ServerHandle;
    processId: string;
    streamType: number;
    text: string;
}

export interface ServerStartingAttributes {
    initiatePolling: boolean;
    request: LaunchParameters;
}

export interface ServerStateChange {
    server: ServerHandle;
    state: number;
}

export interface ServerType {
    id: string;
    visibleName: string;
    description: string;
}

export interface StartServerResponse {
    status: Status;
    details: CommandLineDetails;
}

export interface Status {
    severity: number;
    code: number;
    message: string;
    trace: string;
    ok: boolean;
    plugin: string;
}

export interface StopServerAttributes {
    id: string;
    force: boolean;
}

export interface VMDescription {
    id: string;
    installLocation: string;
    version: string;
}

export interface VMHandle {
    id: string;
}
