import pygame
from OpenGL.GL import *


def mtl_fun(filename):
    contents = {}
    mtl = None
    for line in open(filename, "r"):
        if line.startswith('#'):
            continue
        values = line.split()
        if not values:
            continue
        if values[0] == 'newmtl':
            mtl = contents[values[1]] = {}
        elif mtl is None:
            raise ValueError("mtl file doesn't start with newmtl stmt")
        elif values[0] == 'map_Kd':
            # load the texture referred to by this declaration
            mtl[values[0]] = values[1]
            print("* ", mtl['map_Kd'])
            surf = pygame.image.load(mtl['map_Kd'])
            image = pygame.image.tostring(surf, 'RGBA', True)
            ix, iy = surf.get_rect().size
            texid = mtl['texture_Kd'] = glGenTextures(1)
            # print(texid)
            glBindTexture(GL_TEXTURE_2D, texid)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                            GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                            GL_LINEAR)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, ix, iy, 0, GL_RGBA,
                         GL_UNSIGNED_BYTE, image)
        else:
            # mtl[values[0]] = map(float, values[1:])
            mtl[values[0]] = [float(aux) for aux in values[1:]]
    # print(contents)
    return contents


class OBJ:
    def __init__(self, filename, swapyz=False):
        """Loads a Wavefront OBJ file. """
        self.vertices = []
        self.normals = []
        self.texcoords = []
        self.faces = []

        """the size of the bounding box of the object """
        self.Lx = 0
        self.Ly = 0
        self.Lz = 0

        self.mtl = None
        self.gl_list = None

        # read the values from the file

        self.load_file(filename, swapyz)

        # move the object in the origin of the coordinate system
        self.move_in_origin()

        # create the draw list
        self.create_list()

        # compute the size of the bounding box
        self.bound_box()

    def load_file(self, filename, swapyz):
        material = None
        for line in open(filename, "r"):
            if line.startswith('#'):
                continue
            values = line.split()
            if not values:
                continue
            if values[0] == 'v':
                # v = map(float, values[1:4])
                v = [float(aux) for aux in values[1:4]]
                if swapyz:
                    v = v[0], v[2], v[1]
                self.vertices.append(v)
            elif values[0] == 'vn':
                # v = map(float, values[1:4])
                v = [float(aux) for aux in values[1:4]]
                if swapyz:
                    v = v[0], v[2], v[1]
                self.normals.append(v)
            elif values[0] == 'vt':
                # self.texcoords.append(map(float, values[1:3]))
                self.texcoords.append([float(aux) for aux in values[1:43]])
            elif values[0] in ('usemtl', 'usemat'):
                material = values[1]
            elif values[0] == 'mtllib':
                self.mtl = mtl_fun(values[1])
            elif values[0] == 'f':
                face = []
                texcoords = []
                norms = []
                for v in values[1:]:
                    w = v.split('/')
                    face.append(int(w[0]))
                    if len(w) >= 2 and len(w[1]) > 0:
                        texcoords.append(int(w[1]))
                    else:
                        texcoords.append(0)
                    if len(w) >= 3 and len(w[2]) > 0:
                        norms.append(int(w[2]))
                    else:
                        norms.append(0)
                self.faces.append((face, norms, texcoords, material))

    def scale(self, size=1.0):
        """ scales the loaded object with the factor size """

        aux = [[x * size for x in lines] for lines in self.vertices]
        self.vertices = aux

        # recreate the draw list
        self.create_list()

    def translate(self, cx=0, cy=0, cz=0):
        """ translates the loaded object with cx, cy, cz points """

        aux = [[point[0] + cx, point[1] + cy, point[2] + cz] for point in self.vertices]
        self.vertices = aux

        # recreate the draw list
        self.create_list()

    def move_in_origin(self):
        minx, miny, minz = 1000, 1000, 1000
        for linie in self.vertices:
            if minx > linie[0]:
                minx = linie[0]
            if miny > linie[1]:
                miny = linie[1]
            if minz > linie[2]:
                minz = linie[2]
        aux = [[point[0] - minx, point[1] - miny, point[2] - minz] for point in self.vertices]
        self.vertices = aux

    def bound_box(self):

        minx, miny, minz = 1000, 1000, 1000
        maxx, maxy, maxz = -1000, -1000, -1000

        for linie in self.vertices:
            if minx > linie[0]:
                minx = linie[0]
            if miny > linie[1]:
                miny = linie[1]
            if minz > linie[2]:
                minz = linie[2]
            if maxx < linie[0]:
                maxx = linie[0]
            if maxy < linie[1]:
                maxy = linie[1]
            if maxz > linie[2]:
                maxz = linie[2]

        self.Lx = maxx - minx
        self.Ly = maxy - miny
        self.Lz = maxz - minz

    def create_list(self):
        """ create a draw list that can be called with glCallList """
        self.gl_list = glGenLists(1)
        glNewList(self.gl_list, GL_COMPILE)
        glEnable(GL_TEXTURE_2D)
        glFrontFace(GL_CCW)
        aux_color = [0.5, 0.5, 0.5]
        for face in self.faces:
            vertices, normals, texture_coords, material = face

            mtl = self.mtl[material]
            if 'texture_Kd' in mtl:
                # use diffuse texmap
                glBindTexture(GL_TEXTURE_2D, mtl['texture_Kd'])
            else:
                # just use diffuse colour
                glColor(*mtl['Kd'])

            glBegin(GL_POLYGON)
            for i in range(len(vertices)):
                if normals[i] > 0:
                    glNormal3fv(self.normals[normals[i] - 1])
                if texture_coords[i] > 0:
                    glTexCoord2fv(self.texcoords[texture_coords[i] - 1])
                glColor3fv(aux_color)
                glVertex3fv(self.vertices[vertices[i] - 1])
            glEnd()
        glDisable(GL_TEXTURE_2D)
        glEndList()
        return self.gl_list

    def draw(self):
        glPushMatrix()
        glCallList(self.gl_list)
        glPopMatrix()

# o = OBJ("cube.obj")
